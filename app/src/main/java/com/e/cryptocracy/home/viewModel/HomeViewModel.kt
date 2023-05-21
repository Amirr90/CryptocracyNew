package com.e.cryptocracy.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.e.cryptocracy.home.dtos.CoinResponse
import com.e.cryptocracy.home.events.CoinEvents
import com.e.cryptocracy.home.mappers.toCoinList
import com.e.cryptocracy.home.models.CoinUi
import com.e.cryptocracy.home.repo.HomeRepo
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.BaseViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinRepo: HomeRepo,
    val store: Store<ApplicationState>,
    val repo: HomeRepo,
    val appPrefs: AppPrefs,
) : BaseViewModel(store) {
    val map = HashMap<String, Any>()

    init {
        map["limit"] = AppConstant.DEFAULT_COIN_LIMIT
    }

    fun products() = coinRepo.loadCoins().cachedIn(viewModelScope)

    fun onEvent(event: CoinEvents) {
        when (event) {
            is CoinEvents.SortChangeClicked -> {
                map["orderBy"] = "change"
                map["orderDirection"] = event.changeSort

                updateStore(orderBy = "change", orderDirection = event.changeSort)
            }
            is CoinEvents.SortPriceClicked -> {
                map["orderBy"] = "price"
                map["orderDirection"] = event.priceSort
                updateStore(orderBy = "price", orderDirection = event.priceSort)

            }
            is CoinEvents.SortMarketClicked -> {
                map.remove("orderBy")
                map.remove("orderDirection")
                updateStore(orderBy = "price", orderDirection = event.marketSort)
            }
            is CoinEvents.SortCoinListingLimit -> {
                map["limit"] = event.limit

                refreshCoins()
            }
            CoinEvents.FetchAllCoins -> {
                map["limit"] = "2000"
                refreshCoins()
            }
        }

        Log.d("TAG", "onEventHomeViewModel: $map")
    }

    private fun updateStore(orderBy: String, orderDirection: String) {
        viewModelScope.launch {
            store.update {
                return@update it.copy(
                    orderBy = orderBy,
                    orderDirection = orderDirection
                )
            }

            refreshCoins()
        }
    }

    fun refreshCoins() = viewModelScope.launch {
        loading()
        val data = repo.loadCoinNonPaging(map)
        bindData(data)
    }

    private fun refreshFavCoins(map: HashMap<String, Any>) = viewModelScope.launch {
        loading()
        val data = repo.loadCoinNonPaging(map)
        viewModelScope.launch {
            when (data) {
                is ApiResponse.Failed<*> -> {
                    Log.d("TAG", "refreshCoins: " + data.errorMsg)
                    store.update {
                        return@update it.copy(
                            favouriteCoins = emptyList(),
                            loading = false,
                            errorMsg = data.errorMsg
                        )
                    }
                }
                is ApiResponse.Success<*> -> {
                    val res = data.data as CoinResponse
                    store.update {
                        return@update it.copy(
                            favouriteCoins = res.toCoinList(),
                            loading = false,
                            errorMsg = null
                        )
                    }
                }
            }
        }
    }

    private fun bindData(data: ApiResponse) {
        viewModelScope.launch {
            when (data) {
                is ApiResponse.Failed<*> -> {
                    Log.d("TAG", "refreshCoins: " + data.errorMsg)
                    store.update {
                        return@update it.copy(
                            coins = emptyList(),
                            loading = false,
                            errorMsg = data.errorMsg
                        )
                    }
                }
                is ApiResponse.Success<*> -> {
                    val res = data.data as CoinResponse
                    store.update {
                        return@update it.copy(
                            coins = res.toCoinList(),
                            loading = false,
                            errorMsg = null
                        )
                    }
                }
            }
        }
    }

    fun loadFavouriteCoins(uuids: List<String>) {
        val map = HashMap<String, Any>()
        map["favIds"] = uuids
        refreshFavCoins(map = map)
    }

    fun coins() = combine(
        store.stateFlow.map {
            it.coins
        },
        store.stateFlow.map {
            it.loading
        },
        store.stateFlow.map {
            it.errorMsg
        },

        ) { coinList, loading, errorMsg ->
        CoinUi(
            coins = coinList,
            loading = loading,
            error = errorMsg
        )
    }.distinctUntilChanged().asLiveData()

    fun favouriteCoins() = combine(
        store.stateFlow.map {
            it.favouriteCoins
        },
        store.stateFlow.map {
            it.loading
        },
        store.stateFlow.map {
            it.errorMsg
        },

        ) { favCoinList, loading, errorMsg ->
        CoinUi(
            coins = favCoinList,
            loading = loading,
            error = errorMsg
        )
    }.distinctUntilChanged()

    fun currencyChangeObserver() = store.stateFlow.map {
        it.selectedCurrencyId
    }.distinctUntilChanged()
}
