package com.e.cryptocracy.coinDetail.repo

import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CoinDetailRepoImpl @Inject constructor(
    val api: CoinApi,
    val appPrefs: AppPrefs,
) : CoinDetailRepo {
    override suspend fun loadCoinData(map: HashMap<String, String>): ApiResponse {
        return try {
            val coinId = map["coinId"]!!
            map.remove("coinId")
            map["referenceCurrencyUuid "] = AppConstant.getCurrencyId(appPrefs)
            val req = api.loadCoinDetail(
                coinId,
                map)

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


    override suspend fun loadCoinHistory(map: HashMap<String, String>): ApiResponse {
        return try {
            val coinId = map["coinId"]!!
            map.remove("coinId")
            val req = api.loadCoinHistory(
                coinId,
                map)

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