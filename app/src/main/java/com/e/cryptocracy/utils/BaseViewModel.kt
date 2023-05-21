package com.e.cryptocracy.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.auth.App
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
   private val store: Store<ApplicationState>,
) : ViewModel() {


    fun loading(value: Boolean = true) {
        viewModelScope.launch {
            store.update {
                return@update it.copy(
                    loading = value,
                )
            }
        }

    }
}