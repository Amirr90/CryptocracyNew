package com.e.cryptocracy.currency.repo

import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.utils.ApiResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyRepoImpl @Inject constructor(
    private val api: CoinApi,
) : CurrencyRepo {
    override suspend fun loadCurrency(query: String): ApiResponse {
        return try {
            val res = api.loadCurrency(query)
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