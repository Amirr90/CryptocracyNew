package com.e.cryptocracy.home.controller

import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.CoinHeaderTwoViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel


data class CoinHeaderTwoEpoxyModel(
    private val tag: String,
) : ViewBindingKotlinModel<CoinHeaderTwoViewBinding>(R.layout.coin_header_two_view) {
    override fun CoinHeaderTwoViewBinding.bind() {
        textView6.text = tag
    }
}