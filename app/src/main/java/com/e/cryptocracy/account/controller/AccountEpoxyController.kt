package com.e.cryptocracy.account.controller

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.TypedEpoxyController
import com.e.cryptocracy.account.model.UiAccount
import java.util.*

class AccountEpoxyController(
    private val action: (id: Int) -> Unit,
) :
    TypedEpoxyController<UiAccount>() {
    override fun buildModels(data: UiAccount?) {
        if (null == data)
            return

        data.accountSection.forEach {
            val epoxyId = UUID.randomUUID().toString()

            AccountUiEpoxyModel(data = it, clickListener = { id ->
                action(id)
            }).id(epoxyId)
                .addTo(this)
        }
    }
}