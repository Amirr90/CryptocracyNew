package com.e.cryptocracy.home.controller.search

import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.SearchedMarketViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel
import com.e.cryptocracy.home.dtos.searchDto.SearchedMarket

data class SearchMarketEpoxyModel(
    val data: SearchedMarket,
    val clickListener: (id: String) -> Unit,
) : ViewBindingKotlinModel<SearchedMarketViewBinding>(R.layout.searched_market_view) {
    override fun SearchedMarketViewBinding.bind() {

        searchedMarket = data

        mainSearchedMarketView.setOnClickListener {
            clickListener(data.exchangeUuid)
        }

    }

}
