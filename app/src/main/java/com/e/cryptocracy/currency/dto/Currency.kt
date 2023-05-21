package com.e.cryptocracy.currency.dto

import java.text.DecimalFormat

data class Currency(
    val iconUrl: String,
    val name: String,
    val sign: String?,
    val symbol: String,
    val type: String,
    val uuid: String,
) {

}