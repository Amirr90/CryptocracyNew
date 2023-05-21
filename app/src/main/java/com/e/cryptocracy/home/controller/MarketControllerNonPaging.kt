package com.e.cryptocracy.home.controller

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.TypedEpoxyController
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.navigateToCoinDetailScreen
import java.util.*

class MarketControllerNonPaging(
    val appPrefs: AppPrefs,
    val fragment: Fragment,
) : TypedEpoxyController<List<Coin>>() {
    override fun buildModels(data: List<Coin>?) {
        if (data.isNullOrEmpty()) {

            repeat(10) {
                val epoxyId = UUID.randomUUID().toString()

                CoinEpoxyModel(
                    null, AppConstant.currencySymbol(appPrefs),
                    onCardClick = {
                    }, appPrefs.showPriceInBtc(),
                    appPrefs.getShowGraph()
                ).id(epoxyId).addTo(this)

            }
            return
        }




        data.forEach {
            val epoxyId = UUID.randomUUID().toString()
            CoinEpoxyModel(
                it,
                AppConstant.currencySymbol(appPrefs),
                onCardClick = { coinId ->
                    onCardClick(coinId)
                },
                appPrefs.showPriceInBtc(),
                appPrefs.getShowGraph()

            ).id(epoxyId).addTo(this)
        }
    }

    private fun onCardClick(coin: Coin?) {
        coin?.apply {
            fragment.navigateToCoinDetailScreen(id)
        }

    }

}