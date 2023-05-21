package com.e.cryptocracy.home.controller.search

import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.CustomTextviewAsHeaderBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel

data class CustomHeaderEpoxyModel(
    val title: String,
) :
    ViewBindingKotlinModel<CustomTextviewAsHeaderBinding>(R.layout.custom_textview_as_header) {
    override fun CustomTextviewAsHeaderBinding.bind() {
        tvHeader.text = title
    }
}
