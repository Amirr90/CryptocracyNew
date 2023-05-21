package com.e.cryptocracy.home.events

sealed class CoinEvents {
    class SortPriceClicked(val priceSort: String) : CoinEvents()
    class SortChangeClicked(val changeSort: String) : CoinEvents()
    class SortMarketClicked(val marketSort: String) : CoinEvents()
    class SortCoinListingLimit(val limit: String) : CoinEvents()
    object FetchAllCoins : CoinEvents()
}



