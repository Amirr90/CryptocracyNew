package com.e.cryptocracy.utils

import android.annotation.SuppressLint
import android.util.Log
import com.e.cryptocracy.auth.App
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object AppConstant {

    private const val DEFAULT_CURRENCY_LOGO = "$"
    private const val DEFAULT_CURRENCY_ID = "yhjMzLPhuIDl"
    const val DEFAULT_TIME_PERIOD = "24h"
    const val DEFAULT_COIN_LIMIT = "100"

    fun getCurrencyId(appPrefs: AppPrefs) = when {
        appPrefs.getCurrency() != null -> {
            appPrefs.getCurrency()!!.uuid
        }
        else -> DEFAULT_CURRENCY_ID
    }

    fun currencySymbol(appPrefs: AppPrefs) = when {
        appPrefs.getCurrency() != null -> {
            val currency = appPrefs.getCurrency()!!
            when (currency.sign) {
                null -> {
                    currency.symbol
                }
                else -> appPrefs.getCurrency()!!.sign + ""
            }
        }

        else -> DEFAULT_CURRENCY_LOGO
    }

    fun getValueInRoman(number: Float): String {
        var value = number
        val arr = arrayOf("", "K", "M", "Bn", "Tn", "P", "E")
        var index = 0
        while (value / 1000 >= 1) {
            value /= 1000
            index++
        }
        val decimalFormat = DecimalFormat("#.##")
        return try {
            java.lang.String.format("%s %s", decimalFormat.format(value), arr[index])
        } catch (ex: Exception) {
            ""
        }
    }

    fun formatNumber(number: String): String {
        Log.d("TAG", "formatNumber: $number")
        val formatter = DecimalFormat("###,###,###,###.############")
        return formatter.format(number.toFloat())
    }

    fun formatNumber(number: Double): String {
        Log.d("TAG", "formatNumber: $number")
        val currency = AppPrefs(App.instance).getCurrency()
            ?: return internationalFormat(number).format(number)

        val formatter = when (currency.symbol) {
            "INR" -> {
                if (number.toInt() > 0) {
                    DecimalFormat("##,##,##,##,###.##")
                } else DecimalFormat("##,##,##,##,##,###.###########")
            }
            else -> {
                internationalFormat(number)
            }
        }
        return formatter.format(number)
    }


    @SuppressLint("SimpleDateFormat")
    fun dateFromTimestamp(date: Long, format: String = "dd/MM/yyyy hh:mm:ss"): String {
        val formatter = SimpleDateFormat(format)
        return formatter.format(Date(date))
    }

    private fun internationalFormat(number: Double): DecimalFormat {
        return if (number.toInt() > 0) {
            DecimalFormat("###,###,###,###,###.##")
        } else DecimalFormat("###,###,###,###,###.############")
    }

    fun bindPrice(price: Double): BigDecimal {
        val scale = if (price.toInt() > 0) {
            2
        } else price.toString().split(".").last().find {
            it.toString() == "0"
        }.toString().length
        return BigDecimal(price).setScale(scale + 1, RoundingMode.HALF_UP)
    }

    fun roundToNearDecimal(nearValue: Int, btcPrice: String?): String {

        return btcPrice?.apply {
            BigDecimal(this).setScale(nearValue, RoundingMode.HALF_UP).toString()
        } ?: ""
    }


}



