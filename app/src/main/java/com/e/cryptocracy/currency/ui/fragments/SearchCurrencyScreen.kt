package com.e.cryptocracy.currency.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.e.cryptocracy.currency.controller.SearchCurrencyEpoxyController
import com.e.cryptocracy.currency.viewModel.CurrencyViewModel
import com.e.cryptocracy.databinding.FragmentSearchCurrencyScreenBinding
import com.e.cryptocracy.home.models.TimePeriod
import com.e.cryptocracy.home.models.TimePeriodUi
import com.e.cryptocracy.home.viewModel.HomeViewModel
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.EventsChannel
import com.e.cryptocracy.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class SearchCurrencyScreen : Fragment() {
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var binding: FragmentSearchCurrencyScreenBinding
    private lateinit var controller: SearchCurrencyEpoxyController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        currencyViewModel = ViewModelProvider(this)[CurrencyViewModel::class.java]
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = FragmentSearchCurrencyScreenBinding.inflate(layoutInflater)
        controller =
            SearchCurrencyEpoxyController(currencyViewModel, homeViewModel, requireParentFragment())
        controller.setData(emptyList())
        binding.searchCurrencyEpoxy.setController(controller)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyViewModel.currencyObserver().observe(viewLifecycleOwner) {
            controller.setData(it)
        }
        collectFlow(currencyViewModel.events) {
            when (it) {
                is EventsChannel.Failure -> {
                    binding.currencyNoData.root.isVisible = true
                    binding.searchCurrencyEpoxy.isInvisible = true
                }
                is EventsChannel.Loading -> Log.d("TAG", "onViewCreated: ")
                is EventsChannel.Success -> {
                    binding.currencyNoData.root.isInvisible = true
                    binding.searchCurrencyEpoxy.isVisible = true
                }
            }
        }
        currencyViewModel.loadCurrency("")

        binding.apply {
            editTextTextCurrency.addTextChangedListener { text ->
                text?.apply {
                    when (this.toString().length > 2) {
                        true -> {
                            currencyViewModel.loadCurrency(this.toString())
                        }
                        false -> Log.d("TAG", "onViewCreated: ")
                    }
                }
            }
            currencyNoData.btnRetry.setOnClickListener {
                currencyViewModel.loadCurrency("")
            }
            btnCancel.setOnClickListener {
                if (editTextTextCurrency.text.toString().isEmpty())
                    return@setOnClickListener

                editTextTextCurrency.setText("")
                currencyViewModel.loadCurrency("")

            }


        }


    }
}