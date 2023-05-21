package com.e.cryptocracy.coinDetail.dto

data class Supply(
    val circulating: String,
    val confirmed: Boolean,
    val max: Any,
    val supplyAt: Int,
    val total: String
)