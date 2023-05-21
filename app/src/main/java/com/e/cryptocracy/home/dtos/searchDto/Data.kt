package com.e.cryptocracy.home.dtos.searchDto

data class Data(
    val coins: List<SearchedCoin>,
    val exchanges: List<Any>,
    val markets: List<SearchedMarket>
)