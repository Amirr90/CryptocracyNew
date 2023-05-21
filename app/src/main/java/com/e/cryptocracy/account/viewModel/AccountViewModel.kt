package com.e.cryptocracy.account.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.R
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store

import com.e.cryptocracy.account.model.AccountSectionModel
import com.e.cryptocracy.account.model.UiAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {


    fun refreshAccountData() = viewModelScope.launch {
        store.update {
            return@update it.copy(
                accountData = UiAccount(
                    title = "Account Slogen here",
                    accountSection = listOfAccountData()
                )
            )
        }
    }

    private fun listOfAccountData(): List<AccountSectionModel> {

        return listOf(
            AccountSectionModel(
                image = R.drawable.ic_baseline_supervised_user_circle_24,
                title = "Invite Friends",
                id = 0),
            AccountSectionModel(
                image = R.drawable.ic_baseline_videogame_asset_24,
                title = "Game Zone",
                id = 0),
            AccountSectionModel(
                image = R.drawable.ic_explore_24,
                title = "Candy",
                id = 0),
            AccountSectionModel(
                image = R.drawable.ic_baseline_notifications_active_24,
                title = "Notifications",
                id = 0),
            AccountSectionModel(
                image = R.drawable.ic_baseline_settings_24,
                title = "Setting",
                id = R.id.action_global_marketSettingFragment)
        )
    }

}