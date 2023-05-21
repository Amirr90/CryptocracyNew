package com.e.cryptocracy.home.dtos.searchDto

data class SearchedMarket(
    val baseSymbol: String,
    val baseUuid: String,
    val exchangeIconUrl: String,
    val exchangeName: String,
    val exchangeUuid: String,
    val quoteSymbol: String,
    val quoteUuid: String,
    val recommended: Boolean,
    val uuid: String
)