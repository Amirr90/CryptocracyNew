package com.e.cryptocracy.home.models

data class SearchUi(
    val searchData: SearchedValue,
    val loading: Boolean,
    val error: String?,
    val currencySymbol: String,
)
