package com.e.cryptocracy.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.home.dtos.searchDto.SearchResponse
import com.e.cryptocracy.home.models.SearchUi
import com.e.cryptocracy.home.models.SearchedValue
import com.e.cryptocracy.home.repo.search.SearchRepo
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.ApiResponse
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepo,
    private val store: Store<ApplicationState>,
    private val appPrefs: AppPrefs,
) : ViewModel() {

    private var _query = MutableStateFlow("")
    private val map = HashMap<String, String>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResult = _query.flatMapLatest {
        map["query"] = it
        refreshSearch(map)
        combine(
            store.stateFlow.map { data ->
                data.searchedValue
            },
            store.stateFlow.map { data ->
                data.loading
            },
            store.stateFlow.map { data ->
                data.errorMsg
            })
        { searchValue, loading, error ->
            SearchUi(
                searchData = searchValue,
                loading = loading,
                error = error,
                currencySymbol = AppConstant.currencySymbol(appPrefs)
            )
        }.distinctUntilChanged()

    }

    private fun refreshSearch(map: java.util.HashMap<String, String>) {
        map["referenceCurrencyUuid"] = AppConstant.getCurrencyId(appPrefs)
        viewModelScope.launch {
            when (val res = repo.search(map)) {
                is ApiResponse.Failed<*> -> {
                    store.update {
                        return@update it.copy(
                            loading = false,
                            errorMsg = res.errorMsg
                        )
                    }
                }
                is ApiResponse.Success<*> -> {
                    val data = res.data as SearchResponse
                    store.update {
                        return@update it.copy(
                            loading = false,
                            searchedValue = SearchedValue(
                                coinValue = data.data.coins,
                                marketValue = data.data.markets
                            ),
                            errorMsg = if (
                                data.data.coins.isEmpty() &&
                                data.data.markets.isEmpty() &&
                                data.data.exchanges.isEmpty()
                            ) "no search result found" else null
                        )
                    }
                }
            }
        }
    }


    fun onQueryChange(query: String) {
        _query.value = query
    }
}