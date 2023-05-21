package com.e.cryptocracy.home.dtos.searchDto

data class SearchedCoin(
    val iconUrl: String,
    val name: String,
    val price: String,
    val symbol: String,
    val uuid: String
)