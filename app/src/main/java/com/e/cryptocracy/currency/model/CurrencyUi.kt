package com.e.cryptocracy.currency.model

import com.e.cryptocracy.currency.dto.Currency

data class CurrencyUi(
    val currency: Currency,
    val selected: Boolean = false,
)

