package com.e.cryptocracy.home.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val api: CoinApi,
    private val appPrefs: AppPrefs,
) : HomeRepo {
    override fun loadCoins(): Flow<PagingData<Coin>> {
        val pageConfig = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        )
        return Pager(
            config = pageConfig,
            initialKey = 1
        ) {
            CoinPagingSource(api, appPrefs)
        }.flow
    }


    override suspend fun loadCoinNonPaging(
        map: HashMap<String, Any>,
    ): ApiResponse {
        map["referenceCurrencyUuid"] = AppConstant.getCurrencyId(appPrefs)
        return try {

            val req = if (map.containsKey("favIds")) {
                val favUuids = map["favIds"] as List<String>
                api.loadFavCoins(favUuids, map)
            } else api.loadCoins(map)

            if (req.isSuccessful && req.code() == 200) {
                ApiResponse.Success(req.body())
            } else ApiResponse.Failed(data = null, "Failed to login")
        } catch (ex: HttpException) {
            ApiResponse.Failed(data = null, "HttpException ${ex.localizedMessage}")
        } catch (ex: IOException) {
            ApiResponse.Failed(data = null, "IOException ${ex.localizedMessage}")
        } catch (ex: Exception) {
            ApiResponse.Failed(data = null, "Exception ${ex.localizedMessage}")
        }
    }
}