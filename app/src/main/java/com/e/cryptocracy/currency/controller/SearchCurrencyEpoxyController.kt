package com.e.cryptocracy.currency.controller

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.TypedEpoxyController
import com.e.cryptocracy.currency.model.CurrencyUi
import com.e.cryptocracy.currency.viewModel.CurrencyViewModel
import com.e.cryptocracy.home.viewModel.HomeViewModel
import com.e.cryptocracy.utils.showSnackBar
import java.util.*

class SearchCurrencyEpoxyController(
    private val currencyViewModel: CurrencyViewModel,
    private val homeViewModel: HomeViewModel,
    private val activity: Fragment,
) : TypedEpoxyController<List<CurrencyUi>>() {
    override fun buildModels(data: List<CurrencyUi>?) {
        Log.d("TAG", "SearchCurrencyEpoxyController: $data")

        if (data.isNullOrEmpty()) {
            repeat(30) {
                val epoxyId = UUID.randomUUID().toString()
                SearchCurrencyEpoxyModel(null, currencyListener = { currency ->
                    currencyViewModel.updateCurrency(currency)
                    //homeViewModel.refreshCoins()
                    activity.showSnackBar(activity.requireView(), "Currency changed !!")
                   // activity.findNavController().navigateUp()
                }).id(epoxyId).addTo(this)
            }

            return
        }

        data.forEach {
            SearchCurrencyEpoxyModel(it, currencyListener = { currency ->
                currencyViewModel.updateCurrency(currency)
                activity.showSnackBar(activity.requireView(), "Currency changed !!")
            }).id(it.currency.uuid).addTo(this)
        }

    }
}