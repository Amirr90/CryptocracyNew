package com.e.cryptocracy.home.ui.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.R
import com.e.cryptocracy.auth.App
import com.e.cryptocracy.databinding.FragmentCoinListingScreenBinding
import com.e.cryptocracy.home.controller.MarketControllerNonPaging
import com.e.cryptocracy.home.events.CoinEvents
import com.e.cryptocracy.home.models.HomeData
import com.e.cryptocracy.home.viewModel.HomeViewModel
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class CoinListingScreen : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentCoinListingScreenBinding

    private lateinit var controller: MarketControllerNonPaging

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var appPrefs: AppPrefs

    private var checkedItemForCoinLimit = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        //viewModel.refreshCoins()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoinListingScreenBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = MarketControllerNonPaging(appPrefs, requireParentFragment())
        binding.coinListingEpoxy.setController(controller)

        combine(store.stateFlow.map {
            it.selectedCurrencyId
        }, store.stateFlow.map {
            it.selectedTimePeriodId
        }, store.stateFlow.map {
            it.showPriceInBtc
        }, store.stateFlow.map {
            it.orderBy
        }, store.stateFlow.map {
            it.showGraph
        }
        ) { selectedCurrencyId, selectedTimePeriodId, showPriceInBtcValue, orderByValue, showGraph ->
            HomeData(
                timePeriodId = selectedTimePeriodId,
                currencyId = selectedCurrencyId,
                showPriceInBtc = showPriceInBtcValue,
                showGraph = showGraph
            )


        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) {
            Log.d("TAG", "HomeData: $it")
            updateCurrency(it)

            viewModel.apply {
                map["timePeriodId"] = it.timePeriodId

                refreshCoins()
            }

        }

        viewModel.coins().observe(viewLifecycleOwner) {
            controller.setData(it.coins)
            binding.apply {
                progressBar2.isVisible = it.loading
                errorLayout.isVisible = it.error != null
                coinListingEpoxy.isInvisible = it.error != null
            }
        }
        setListeners()
        setFilterData()
    }

    private fun setFilterData() {
        binding.apply {
            updateSelectedBackground(coinFilterLayout.tvMarketSort)
        }
    }

    private fun updateCurrency(homeData: HomeData) {
        appPrefs.getCurrency()?.apply {
            binding.apply {
                var currency = symbol
                if (null != sign) {
                    currency += "($sign)"
                }
                binding.tvCurrency.text = currency
                binding.btnTimePeriod.text = homeData.timePeriodId
            }
        }

    }

    private fun setListeners() {
        binding.apply {

            btnRetry.setOnClickListener {
            }
            tvCurrency.setOnClickListener {
                findNavController().navigate(R.id.action_global_searchCurrencyScreen)
            }
            btnTimePeriod.setOnClickListener {
                findNavController().navigate(R.id.action_global_timePeriodBottomDialogFragment)
            }
            ivSetting.setOnClickListener {
                findNavController().navigate(R.id.action_global_marketSettingFragment)
            }
            tvCoinLimit.setOnClickListener {
                handleCoinLimitValue(it)
            }

            tvCoinStats.setOnClickListener {
                findNavController().navigate(R.id.action_global_globalStatsScreen)
            }
            coinFilterLayout.tvSortPrice.setOnClickListener {
                coinFilterLayout.tvSortChange.apply {
                    setCompoundDrawablesWithIntrinsicBounds(0,
                        0,
                        0,
                        0)
                    tag = "0"
                    removeBackground(this)
                }

                val sortValue = handleTag(it)
                val event = CoinEvents.SortPriceClicked(sortValue)
                viewModel.onEvent(event)
            }
            coinFilterLayout.tvSortChange.setOnClickListener {
                coinFilterLayout.tvSortPrice.apply {
                    setCompoundDrawablesWithIntrinsicBounds(0,
                        0,
                        0,
                        0)
                    tag = "0"
                    removeBackground(this)
                }

                val sortValue = handleTag(it)
                val event = CoinEvents.SortChangeClicked(sortValue)
                viewModel.onEvent(event)
            }
            coinFilterLayout.tvMarketSort.setOnClickListener {
                coinFilterLayout.tvSortPrice.apply {
                    setCompoundDrawablesWithIntrinsicBounds(0,
                        0,
                        0,
                        0)
                    tag = "0"
                    removeBackground(this)
                }
                coinFilterLayout.tvSortChange.apply {
                    setCompoundDrawablesWithIntrinsicBounds(0,
                        0,
                        0,
                        0)
                    tag = "0"
                    removeBackground(this)
                }
                updateSelectedBackground(it as TextView)
                val event = CoinEvents.SortMarketClicked("asc")
                viewModel.onEvent(event)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleCoinLimitValue(it: View) {
        val tv = it as TextView
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose One")
            .setSingleChoiceItems(R.array.choices, checkedItemForCoinLimit
            ) { _, _ ->
            }
            .setPositiveButton("OK"
            ) { dialog, _ ->
                val selectedPosition: Int =
                    (dialog as AlertDialog).listView.checkedItemPosition
                checkedItemForCoinLimit = selectedPosition
                val options =
                    requireActivity().resources.getStringArray(R.array.choices)
                val event = CoinEvents.SortCoinListingLimit(options[selectedPosition])
                viewModel.onEvent(event)
                tv.text = "Top ${options[selectedPosition]}"

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    private fun handleTag(view: View): String {
        Log.d("TAG", "handleTag: $tag")
        val tv = view as TextView

        var drawIcon = 0
        val value = when (view.tag as String) {
            "0" -> {
                drawIcon = R.drawable.ic_baseline_arrow_drop_down_24
                view.tag = "1"
                "asc"
            }
            "1" -> {
                drawIcon = R.drawable.ic_baseline_arrow_drop_up_24
                view.tag = "0"
                "desc"

            }
            else -> {
                ""
            }

        }

        tv.setCompoundDrawablesWithIntrinsicBounds(0,
            0,
            drawIcon,
            0)

        updateSelectedBackground(view)
        return value

    }

    private fun updateSelectedBackground(view: TextView) {
        view.background =
            ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_search)
    }

    private fun removeBackground(view: TextView) {
        view.background = null
        binding.coinFilterLayout.tvMarketSort.background = null
    }
}