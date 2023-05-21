package com.e.cryptocracy.home.repo.search

import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.utils.ApiResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    val coinApi: CoinApi,
) : SearchRepo {
    override suspend fun search(map: HashMap<String, String>): ApiResponse {
        return try {
            val req = coinApi.search(map)

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