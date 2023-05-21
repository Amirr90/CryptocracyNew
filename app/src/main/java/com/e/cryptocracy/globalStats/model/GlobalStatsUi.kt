package com.e.cryptocracy.globalStats.model

import com.e.cryptocracy.globalStats.dto.BestCoin
import com.e.cryptocracy.globalStats.dto.NewestCoin

data class GlobalStatsUi(
    val loading: Boolean,
    val error: String?,
    val newCoins: List<NewestCoin> = emptyList(),
    val bestCoin: List<BestCoin> = emptyList(),
    val statsData: StatsData?,
)
