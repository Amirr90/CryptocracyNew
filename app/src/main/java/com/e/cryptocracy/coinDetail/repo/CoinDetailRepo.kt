package com.e.cryptocracy.coinDetail.repo

import com.e.cryptocracy.utils.ApiResponse

interface CoinDetailRepo {
    suspend fun loadCoinData(map: HashMap<String, String>): ApiResponse
    suspend fun loadCoinHistory(map: HashMap<String, String>): ApiResponse
}