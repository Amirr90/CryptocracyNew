package com.e.cryptocracy.currency.controller

import android.util.Log
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.e.cryptocracy.R
import com.e.cryptocracy.currency.dto.Currency
import com.e.cryptocracy.currency.model.CurrencyUi
import com.e.cryptocracy.databinding.SearchCurrencyViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel

data class SearchCurrencyEpoxyModel(
    val currencyData: CurrencyUi?,
    val currencyListener: (currencyData: Currency) -> Unit,
) :
    ViewBindingKotlinModel<SearchCurrencyViewBinding>(R.layout.search_currency_view) {
    override fun SearchCurrencyViewBinding.bind() {
        shimmerLayoutCurrency.isVisible = currencyData == null
        mainCurrencyView.isInvisible = currencyData == null
        Log.d("TAG", "SearchCurrencyEpoxyModel: $currencyData")

        currencyData?.let { data ->
            currency = data.currency

            mainCurrencyView.setOnClickListener {
                currencyListener(data.currency)
            }

            mainCurrencyView.isChecked = data.selected
        }

    }
}
