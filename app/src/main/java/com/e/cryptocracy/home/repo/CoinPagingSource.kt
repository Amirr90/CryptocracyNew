package com.e.cryptocracy.home.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.home.mappers.toCoinList
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppConstant.getCurrencyId
import com.e.cryptocracy.utils.AppPrefs

class CoinPagingSource(
    private val api: CoinApi,
    val appPrefs: AppPrefs,
) : PagingSource<Int, Coin>() {
    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val page = params.key ?: 0
        val offSet = page * 100
        return try {

            val currencyId = getCurrencyId(appPrefs)
            Log.d("TAG", "currencyId: $currencyId")

            val map = HashMap<String, Any>()
            map["offset"] = offSet.toString()
            //map["referenceCurrencyUuid"] = currencyId
            map["scopeLimit"] = "100"
            map["timePeriod"] =
                appPrefs.getValue(AppPrefs.TIME_PERIOD).ifEmpty { AppConstant.DEFAULT_TIME_PERIOD }


            val response = api.loadCoins(map)

            return when {
                response.isSuccessful && response.code() == 200 -> {
                    LoadResult.Page(
                        data = response.body()!!.toCoinList(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (response.body()!!.data.coins.isEmpty()) null else page + 1
                    )
                }
                else -> {
                    LoadResult.Error(Throwable("No data Found!!"))
                }
            }
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

}