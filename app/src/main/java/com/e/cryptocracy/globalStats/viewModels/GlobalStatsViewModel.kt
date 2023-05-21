package com.e.cryptocracy.globalStats.viewModels

import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.globalStats.dto.GlobalStatsResponse
import com.e.cryptocracy.globalStats.mapper.toStatsData
import com.e.cryptocracy.globalStats.model.GlobalStatsUi
import com.e.cryptocracy.globalStats.repo.GlobalStatsRepo
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalStatsViewModel @Inject constructor(
    val repo: GlobalStatsRepo,
    val store: Store<ApplicationState>,
) : BaseViewModel(store) {

    init {
        loadStatsData()
    }

    private fun loadStatsData() {
        viewModelScope.launch {
            loading()
            when (val response = repo.loadStatsData()) {
                is ApiResponse.Failed<*> -> {
                    store.update {
                        return@update it.copy(
                            loading = false,
                            errorMsg = response.errorMsg,

                            )
                    }
                }
                is ApiResponse.Success<*> -> {
                    val data = response.data as GlobalStatsResponse
                    store.update {
                        return@update it.copy(
                            loading = false,
                            newCoins = data.data.newestCoins,
                            bestCoin = data.data.bestCoins,
                            statsData = data.toStatsData()
                        )
                    }
                }


            }
        }
    }

    fun statsData() = combine(
        store.stateFlow.map { it.loading },
        store.stateFlow.map { it.errorMsg },
        store.stateFlow.map { it.newCoins },
        store.stateFlow.map { it.bestCoin },
        store.stateFlow.map { it.statsData },
    ) { loading, errorMsg, newCoinList, bestCoinList, statsData ->
        GlobalStatsUi(
            loading = loading,
            error = errorMsg,
            newCoins = newCoinList,
            bestCoin = bestCoinList,
            statsData = statsData
        )
    }.distinctUntilChanged()
}