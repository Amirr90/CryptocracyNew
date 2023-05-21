package com.e.cryptocracy.currency.repo

import com.e.cryptocracy.utils.ApiResponse

interface CurrencyRepo {
    suspend fun loadCurrency(query: String): ApiResponse
}