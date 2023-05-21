package com.e.cryptocracy.home.models

data class TimePeriodUi(
    val selected: Boolean = false,
    val timePeriodData: TimePeriod,
) {
}

data class TimePeriod(
    val value: String,
)