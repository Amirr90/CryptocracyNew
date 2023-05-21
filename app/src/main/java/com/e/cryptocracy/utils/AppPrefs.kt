package com.e.cryptocracy.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.e.cryptocracy.currency.dto.Currency
import com.google.gson.Gson
import javax.inject.Inject


class AppPrefs @Inject constructor(
    application: Application,
) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveCurrency(currency: Currency) {
        val currencyInJson = Gson().toJson(currency)
        Log.d("TAG", "currencyInJson: $currencyInJson")
        editor.putString(CURRENCY, currencyInJson)
        editor.commit()
    }


    fun getCurrency(): Currency? {
        val currencyInJson = sharedPreferences.getString(CURRENCY, "")
        val currency = Gson().fromJson(currencyInJson, Currency::class.java)
        Log.d("TAG", "getCurrency: $currency")
        return currency
    }

    companion object {
        const val CURRENCY = "currency"
        const val PRICE_IN_BTC = "priceInBtc"
        const val TIME_PERIOD = "timePeriod"
        const val SHOW_GRAPH = "showGraph"
    }

    fun showPriceInBtc(): Boolean {
        return sharedPreferences.getBoolean(PRICE_IN_BTC, false)
    }

    fun savePriceInBtcValue(value: Boolean) {
        editor.putBoolean(PRICE_IN_BTC, value)
        commit()
    }

    fun saveShowGraph(value: Boolean) {
        editor.putBoolean(SHOW_GRAPH, value)
        commit()
    }

    fun getShowGraph(): Boolean {
        return sharedPreferences.getBoolean(SHOW_GRAPH, false)
    }

    fun saveValue(key: String, value: String) {
        editor.putString(key, value)
        commit()
    }

    fun getValue(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    private fun commit() {
        editor.commit()
    }

}