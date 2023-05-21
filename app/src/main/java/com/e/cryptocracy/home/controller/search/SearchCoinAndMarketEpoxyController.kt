package com.e.cryptocracy.home.controller.search

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.e.cryptocracy.home.models.SearchUi
import com.e.cryptocracy.utils.navigateToCoinDetailScreen
import java.util.*

class SearchCoinAndMarketEpoxyController(
    val fragment: Fragment,
) : TypedEpoxyController<SearchUi>() {
    override fun buildModels(data: SearchUi?) {
        if (null == data) {
            return
        }

        if (data.searchData.coinValue.isNotEmpty()) {
            val coinHeading = listOf("Coin").map {
                val epoxyId = UUID.randomUUID().toString()
                CustomHeaderEpoxyModel(it).id(epoxyId)
            }
            CarouselModel_().models(coinHeading).id("coin").addTo(this)
        }


        data.searchData.coinValue.forEach {
            SearchedCoinEpoxyModel(
                it,
                data.currencySymbol, coinClickListener = { id ->
                    onCoinClick(id)
                }
            ).id(it.uuid).addTo(this)
        }

        if (data.searchData.marketValue.isNotEmpty()) {
            val marketHeading = listOf("Market").map {
                val epoxyId = UUID.randomUUID().toString()
                CustomHeaderEpoxyModel(it).id(epoxyId)
            }
            CarouselModel_().models(marketHeading).id("market").addTo(this)
        }

        data.searchData.marketValue.forEach {
            SearchMarketEpoxyModel(it, clickListener = {
            }).id(it.exchangeUuid).addTo(this)
        }

    }

    private fun onCoinClick(id: String) {
        fragment.navigateToCoinDetailScreen(id)
    }
}