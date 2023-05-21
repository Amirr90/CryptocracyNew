package com.e.cryptocracy.account.model

data class UiAccount(
    val accountSection: List<AccountSectionModel> = emptyList(),
    val title: String = "",
)

data class AccountSectionModel(
    val title: String,
    val image: Int,
    val id: Int,
)