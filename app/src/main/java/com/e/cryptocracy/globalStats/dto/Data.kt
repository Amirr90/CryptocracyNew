package com.e.cryptocracy.globalStats.dto

data class Data(
    val bestCoins: List<BestCoin>,
    val btcDominance: Double,
    val newestCoins: List<NewestCoin>,
    val referenceCurrencyRate: Double,
    val total24hVolume: String,
    val totalCoins: Int,
    val totalExchanges: Int,
    val totalMarketCap: String,
    val totalMarkets: Int
)