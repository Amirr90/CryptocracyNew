package com.e.cryptocracy.account.controller

import com.e.cryptocracy.R
import com.e.cryptocracy.account.model.AccountSectionModel
import com.e.cryptocracy.databinding.AccountTypeListViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel


data class AccountUiEpoxyModel(
    val data: AccountSectionModel,
    val clickListener: (id: Int) -> Unit,
) :
    ViewBindingKotlinModel<AccountTypeListViewBinding>(R.layout.account_type_list_view) {

    override fun AccountTypeListViewBinding.bind() {
        textView5.text = data.title
        mainView.setOnClickListener {
            clickListener(data.id)
        }
        if (data.image != 0)
            ivAccountIcon.setImageResource(data.image)
    }
}