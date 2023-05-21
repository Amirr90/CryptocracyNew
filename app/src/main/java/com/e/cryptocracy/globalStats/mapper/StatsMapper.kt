package com.e.cryptocracy.globalStats.mapper

import com.e.cryptocracy.globalStats.dto.GlobalStatsResponse
import com.e.cryptocracy.globalStats.model.StatsData
import com.e.cryptocracy.utils.AppConstant
import java.math.BigDecimal
import java.math.RoundingMode


fun GlobalStatsResponse.toStatsData(): StatsData {
    val data = this.data

    return StatsData(
        referenceCurrencyRate = BigDecimal(data.referenceCurrencyRate).setScale(2,
            RoundingMode.CEILING).toString(),
        totalCoins = AppConstant.formatNumber(data.totalCoins.toString()),
        totalMarkets = AppConstant.formatNumber(data.totalMarkets.toString()),
        totalExchanges = AppConstant.formatNumber(data.totalExchanges.toString()),
        totalMarketCap = AppConstant.getValueInRoman(data.totalMarketCap.toFloat()),
        total24hVolume = AppConstant.getValueInRoman(data.total24hVolume.toFloat()),
        btcDominance = BigDecimal(data.btcDominance).setScale(2, RoundingMode.HALF_UP).toString(),
    )
}