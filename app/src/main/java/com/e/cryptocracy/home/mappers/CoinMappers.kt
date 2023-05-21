package com.e.cryptocracy.home.mappers

import android.util.Log
import com.e.cryptocracy.home.dtos.CoinResponse
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppConstant.getValueInRoman
import java.math.BigDecimal
import java.math.RoundingMode

fun CoinResponse.toCoinList(): List<Coin> {
    return this.data.coins.map {

        Coin(
            name = it.name,
            symbol = it.symbol,
            image = it.iconUrl ?: "",
            id = it.uuid,
            price = bindPrice(it.price.toDouble()),
            btcPrice = BigDecimal(it.btcPrice).setScale(10, RoundingMode.CEILING),
            marketCap = getValueInRoman(if (it.marketCap == null) 0.0f else it.marketCap.toFloat()),
            rank = it.rank,
            change = it.change,
            color = it.color,
            sparkLine = getSparkLine(it.sparkline),
            listedAt = it.listedAt.toLong(),
            volume24h = it.volume24
        )
    }
}

fun getSparkLine(sparkline: List<String?>): FloatArray {
    val list: MutableList<Float> = ArrayList()
    sparkline.forEach {
        it?.apply {
            list.add(this.toFloat())
        }
    }
    return list.toFloatArray()
}

fun bindPrice(price: Double): String {
    val scale = if (price.toInt() > 0) {
        2
    } else price.toString().split(".").last().find {
        it.toString() == "0"
    }.toString().length
    BigDecimal(price).setScale(scale + 1, RoundingMode.HALF_UP)
    return AppConstant.formatNumber(price)
}
