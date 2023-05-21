package com.e.cryptocracy.home.models

data class HomeData(
    val timePeriodId: String = "24h",
    val currencyId: String = "24h",
    val showPriceInBtc: Boolean,
    val showGraph: Boolean,
)
