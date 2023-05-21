package com.e.cryptocracy.home.repo

import androidx.paging.PagingData
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    fun loadCoins(): Flow<PagingData<Coin>>
    suspend fun loadCoinNonPaging(map: HashMap<String, Any>): ApiResponse

}