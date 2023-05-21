package com.e.cryptocracy.home.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.cryptocracy.databinding.FragmentExchangeScreenBinding
import com.e.cryptocracy.home.controller.MarketControllerNonPaging
import com.e.cryptocracy.home.events.CoinEvents
import com.e.cryptocracy.home.viewModel.HomeViewModel
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppPrefs
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ExchangeScreen : Fragment() {
    private lateinit var controller: MarketControllerNonPaging
    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var appPrefs: AppPrefs

    lateinit var binding: FragmentExchangeScreenBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExchangeScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        controller = MarketControllerNonPaging(appPrefs, requireParentFragment())
        binding.coinListingEpoxy.setController(controller)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setListeners()

        viewModel.coins().observe(viewLifecycleOwner) {
            val coins = it.coins.sortedBy { coin ->
                coin.volume24h
            }
            /*repeat(50) { count ->
                Log.d("TAG",
                    "onViewCreatedrepeat: " + coins[count].volume24h)
            }
            controller.setData(coins)*/
            binding.apply {
                coinListingEpoxy.isInvisible = it.error != null
            }
        }
    }

    private fun setListeners() {
        viewModel.onEvent(CoinEvents.FetchAllCoins)
    }

}