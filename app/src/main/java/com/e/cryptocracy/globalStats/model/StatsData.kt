package com.e.cryptocracy.globalStats.model

data class StatsData(
    val referenceCurrencyRate: String,
    val totalCoins: String,
    val totalMarkets: String,
    val totalExchanges: String,
    val totalMarketCap: String,
    val total24hVolume: String,
    val btcDominance: String,
)
