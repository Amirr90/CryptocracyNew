package com.e.cryptocracy.coinDetail.viewModel

import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.appEvents.OnFavouriteCoinEvent
import com.e.cryptocracy.coinDetail.dto.CoinDetailResponse
import com.e.cryptocracy.coinDetail.dto.coinHistory.CoinHistoryResponse
import com.e.cryptocracy.coinDetail.model.CoinDetailUI
import com.e.cryptocracy.coinDetail.repo.CoinDetailRepo
import com.e.cryptocracy.firebaseDatabase.UpdateFavouriteIds
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    appPrefs: AppPrefs,
    private val store: Store<ApplicationState>,
    private val onFavouriteCoinEvent: OnFavouriteCoinEvent,
    private val repo: CoinDetailRepo,
    private val updateFavouriteIds: UpdateFavouriteIds,
) : BaseViewModel(store) {

    val map = HashMap<String, String>()

    init {
        map["referenceCurrencyUuid"] = AppConstant.getCurrencyId(appPrefs)

    }

    fun loadCoinData(coinId: String) {
        map["coinId"] = coinId
        viewModelScope.launch {
            when (val data = repo.loadCoinData(map)) {
                is ApiResponse.Failed<*> -> {
                    store.update {
                        return@update it.copy(
                            loading = false,
                            errorMsg = data.errorMsg
                        )
                    }
                }
                is ApiResponse.Success<*> -> {
                    val response = data.data as CoinDetailResponse

                    store.update {
                        return@update it.copy(
                            coinDetail = response.data.coin,
                            loading = false,
                            errorMsg = null,
                        )
                    }

                }
            }
        }
    }

    fun updateFavourite(coinId: String) {
        viewModelScope.launch {
            store.update { currentState ->
                onFavouriteCoinEvent.invoke(currentState, coinId)
            }
        }

    }

    fun coin(coinId: String) = combine(
        store.stateFlow.map {
            it.coinDetail
        },
        store.stateFlow.map {
            it.favouriteCoinIds
        }, store.stateFlow.map {
            it.coinHistory
        },
        store.stateFlow.map {
            it.priceChange
        }
    ) { coinDetail, setOfFavouriteIds, coinHistory, changeInPrice ->
        CoinDetailUI(
            coinData = coinDetail,
            isFavourite = setOfFavouriteIds.contains(coinId),
            coinHistoryData = coinHistory,
            change = changeInPrice
        )
    }.distinctUntilChanged()


    fun refreshCoinHistory(coinId: String, key: String = AppConstant.DEFAULT_TIME_PERIOD) {
        map["timePeriod"] = key
        map["coinId"] = coinId

        viewModelScope.launch {
            loading()
            when (val response = repo.loadCoinHistory(map)) {
                is ApiResponse.Failed<*> -> {
                    store.update {
                        return@update it.copy(
                            loading = false,
                            errorMsg = response.errorMsg
                        )
                    }
                }

                is ApiResponse.Success<*> -> {
                    val data = response.data as CoinHistoryResponse

                    store.update {
                        return@update it.copy(
                            coinHistory = data.data.history,
                            loading = false,
                            errorMsg = null,
                            priceChange = data.data.change ?: ""
                        )
                    }

                }
            }
        }
    }
}