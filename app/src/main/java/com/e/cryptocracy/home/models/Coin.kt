package com.e.cryptocracy.home.models

import java.math.BigDecimal

data class Coin(
    val name: String,
    val price: String,
    val marketCap: String,
    val image: String,
    val symbol: String,
    val id: String,
    val rank: Int,
    val change: String? = null,
    val btcPrice: BigDecimal,
    val color: String? = null,
    val sparkLine: FloatArray,
    val listedAt: Long,
    val volume24h: String,
)
