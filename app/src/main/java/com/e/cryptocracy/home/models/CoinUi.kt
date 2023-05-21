package com.e.cryptocracy.home.models

data class CoinUi(
    val coins: List<Coin> = emptyList(),
    val loading: Boolean,
    val error: String? = null,
)