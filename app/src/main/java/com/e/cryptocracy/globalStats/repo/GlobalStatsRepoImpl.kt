package com.e.cryptocracy.globalStats.repo

import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GlobalStatsRepoImpl @Inject constructor(
    val api: CoinApi,
    val appPrefs: AppPrefs,
) : GlobalStatsRepo {
    override suspend fun loadStatsData(): ApiResponse {
        return try {
            val currencyId = AppConstant.getCurrencyId(appPrefs)
            val res = api.statsData(currencyId)
            when {
                res.isSuccessful && res.code() == 200 -> {
                    ApiResponse.Success(res.body())
                }
                else -> ApiResponse.Failed(
                    data = null,
                    errorMsg = "Failed to get Data ${res.code()}"
                )
            }
        } catch (ex: IOException) {
            ApiResponse.Failed(data = null, errorMsg = "Failed to get Data ${ex.localizedMessage}")

        } catch (ex: HttpException) {
            ApiResponse.Failed(data = null, errorMsg = "Failed to get Data ${ex.localizedMessage}")
        }
    }
}