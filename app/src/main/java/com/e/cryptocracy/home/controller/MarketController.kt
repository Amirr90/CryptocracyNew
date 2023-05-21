package com.e.cryptocracy.home.controller

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import java.util.*

class MarketController(
    private val appPrefs: AppPrefs,
) : PagingDataEpoxyController<Coin>() {


    override fun buildItemModel(currentPosition: Int, item: Coin?): EpoxyModel<*> {
        val epoxyId = UUID.randomUUID().toString()

        if (item == null) {
            repeat(10) {
                return CoinEpoxyModel(
                    item, AppConstant.currencySymbol(appPrefs),
                    onCardClick = {
                    }, appPrefs.showPriceInBtc(),
                    appPrefs.getShowGraph()
                ).id(epoxyId)
            }

        }
        val list = listOf("USD", "24h")
        val uiHeader2 = list.map {
            CoinHeaderTwoEpoxyModel(it).id(UUID.randomUUID().toString())
        }

        // CarouselModel_().models(uiHeader2).id("filter").addTo(this)
        return CoinEpoxyModel(
            item,
            AppConstant.currencySymbol(appPrefs),
            onCardClick = {
                onCardClick()
            }, appPrefs.showPriceInBtc(),
            appPrefs.showPriceInBtc()
        ).id(epoxyId)
    }


    private fun onCardClick() {

    }
}