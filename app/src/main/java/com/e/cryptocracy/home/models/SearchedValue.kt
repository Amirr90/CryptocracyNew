package com.e.cryptocracy.home.models

import com.e.cryptocracy.home.dtos.searchDto.SearchedCoin
import com.e.cryptocracy.home.dtos.searchDto.SearchedMarket

data class SearchedValue(
    val coinValue: List<SearchedCoin> = emptyList(),
    val marketValue: List<SearchedMarket> = emptyList(),
)