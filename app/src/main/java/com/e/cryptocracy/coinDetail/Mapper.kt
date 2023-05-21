package com.e.cryptocracy.coinDetail

import com.e.cryptocracy.coinDetail.model.CoinDetailUI

fun CoinDetailUI.toGraphData(): List<List<Number>> {
    val data = this.coinHistoryData
    val list: MutableList<List<Number>> = ArrayList()
    data.map {
        it.price?.apply {
            list.add(listOf<Number>(it.timestamp, this.toDouble()))
        }
    }
    return list
}

