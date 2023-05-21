package com.e.cryptocracy.home.controller.search

import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.SearchedCoinViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel
import com.e.cryptocracy.home.dtos.searchDto.SearchedCoin

data class SearchedCoinEpoxyModel(
    val data: SearchedCoin,
    val currencySymbol: String,
    val coinClickListener: (id: String) -> Unit,
) : ViewBindingKotlinModel<SearchedCoinViewBinding>(R.layout.searched_coin_view) {
    override fun SearchedCoinViewBinding.bind() {
        searchedCoin = data
        currency = currencySymbol
        mainSearchedCoinView.setOnClickListener {
            coinClickListener(data.uuid)
        }
    }
}
