package com.e.cryptocracy.coinDetail.model

import com.e.cryptocracy.coinDetail.dto.CoinDetail
import com.e.cryptocracy.coinDetail.dto.coinHistory.History

data class CoinDetailUI(
    val isFavourite: Boolean = false,
    val coinData: CoinDetail? = null,
    val coinHistoryData: ArrayList<History> = ArrayList(),
    val change: String = "",
    val bindHistoryData: List<Array<Number>> = emptyList(),
)


