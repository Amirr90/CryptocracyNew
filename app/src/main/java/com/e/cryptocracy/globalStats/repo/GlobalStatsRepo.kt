package com.e.cryptocracy.globalStats.repo

import com.e.cryptocracy.utils.ApiResponse

interface GlobalStatsRepo {
    suspend fun loadStatsData(): ApiResponse
}