package com.e.cryptocracy.currency.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.currency.dto.Currency
import com.e.cryptocracy.currency.dto.CurrencyResponse
import com.e.cryptocracy.currency.model.CurrencyUi
import com.e.cryptocracy.currency.repo.CurrencyRepo
import com.e.cryptocracy.home.models.TimePeriod
import com.e.cryptocracy.home.models.TimePeriodUi
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.EventsChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val store: Store<ApplicationState>,
    private val currencyRepo: CurrencyRepo,
    private val appPrefs: AppPrefs,
) : ViewModel() {
    private val _events = Channel<EventsChannel>()
    val events = _events.receiveAsFlow()

    fun loadCurrency(searchKey: String) = viewModelScope.launch {
        _events.send(EventsChannel.Loading(true))
        when (val currencyData = currencyRepo.loadCurrency(searchKey)) {

            is ApiResponse.Failed<*> -> {
                _events.send(EventsChannel.Loading(false))
                _events.send(EventsChannel.Failure(errorMsg = currencyData.errorMsg))
            }
            is ApiResponse.Success<*> -> {
                _events.send(EventsChannel.Loading(false))
                val data = currencyData.data as CurrencyResponse
                _events.send(EventsChannel.Success(data = data))

                store.update {
                    it.copy(
                        currencyList = data.data.currencies
                    )
                }
                appPrefs.getCurrency()?.apply {
                    store.update {
                        return@update it.copy(
                            selectedCurrencyId = uuid
                        )
                    }
                }

            }
        }
    }

    fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            store.update {
                return@update it.copy(
                    selectedCurrencyId = currency.uuid
                )
            }
            appPrefs.saveCurrency(currency)
        }

    }

    fun data() = combine(
        store.stateFlow.map { it.timePeriodData },
        store.stateFlow.map { it.selectedTimePeriodId }
    ) { currencyList, selectedId ->
        currencyList.map {
            TimePeriodUi(
                timePeriodData = TimePeriod(value = it),
                selected = selectedId.contains(it)
            )
        }
    }.distinctUntilChanged().asLiveData()

    fun currencyObserver() = combine(
        store.stateFlow.map { it.currencyList },
        store.stateFlow.map { it.selectedCurrencyId }
    ) { currencyList, selectedId ->
        currencyList.map {
            CurrencyUi(
                currency = it,
                selected = selectedId.contains(it.uuid)
            )
        }
    }.distinctUntilChanged().asLiveData()


}