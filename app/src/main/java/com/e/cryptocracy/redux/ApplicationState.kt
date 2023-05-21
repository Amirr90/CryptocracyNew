package com.e.cryptocracy.redux

import com.e.cryptocracy.coinDetail.dto.CoinDetail
import com.e.cryptocracy.coinDetail.dto.coinHistory.History
import com.e.cryptocracy.currency.dto.Currency
import com.e.cryptocracy.currency.model.CurrencyUi
import com.e.cryptocracy.home.models.SearchedValue
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.account.model.UiAccount
import com.e.cryptocracy.globalStats.dto.BestCoin
import com.e.cryptocracy.globalStats.dto.NewestCoin
import com.e.cryptocracy.globalStats.model.StatsData

data class ApplicationState(
    val favouriteProductIds: Set<Int> = emptySet(),
    val selectedCurrencyId: String = "",
    val currency: List<CurrencyUi> = emptyList(),
    val favouriteCoinIds: Set<String> = emptySet(),
    val currencyList: List<Currency> = emptyList(),


    val timePeriodData: List<String> = emptyList(),
    val selectedTimePeriodId: String = "24h",

    val showPriceInBtc: Boolean = false,
    val showGraph: Boolean = false,

    val orderBy: String? = null,
    val orderDirection: String? = null,

    val coins: List<Coin> = emptyList(),
    val favouriteCoins: List<Coin> = emptyList(),
    val loading: Boolean = true,
    val errorMsg: String? = null,
    val searchedValue: SearchedValue = SearchedValue(
        emptyList(),
        emptyList()
    ),

    val coinDetail: CoinDetail? = null,
    val coinHistory: ArrayList<History> = ArrayList(),
    val priceChange: String = "",

    val accountData: UiAccount = UiAccount(),
    val newCoins: List<NewestCoin> = emptyList(),
    val bestCoin: List<BestCoin> = emptyList(),
    val statsData: StatsData? = null,

    val userLoggedIn: Boolean = false,

    )
