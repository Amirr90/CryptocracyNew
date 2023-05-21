package com.e.cryptocracy.coinDetail.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.R
import com.e.cryptocracy.auth.App
import com.e.cryptocracy.coinDetail.coinUtil.TabSelection
import com.e.cryptocracy.coinDetail.graph.Graph
import com.e.cryptocracy.coinDetail.model.CoinDetailUI
import com.e.cryptocracy.coinDetail.viewModel.CoinDetailViewModel
import com.e.cryptocracy.databinding.FragmentCoinDetailScreenBinding
import com.e.cryptocracy.home.spark.CustomSparkAdapter
import com.e.cryptocracy.home.ui.HomeActivity
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class CoinDetailScreen : Fragment() {
    lateinit var binding: FragmentCoinDetailScreenBinding
    lateinit var viewModel: CoinDetailViewModel
    val map = HashMap<String, String>()

    private var coinId: String? = null

    @Inject
    lateinit var appPrefs: AppPrefs

    @Inject
    lateinit var store: Store<ApplicationState>
    private lateinit var graph: Graph

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoinDetailScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[CoinDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinId = requireArguments().getString("coinId")


        when (coinId) {
            null -> {
                findNavController().navigateUp()
            }
            else -> {
                map["coinId"] = coinId!!
                bindData()
            }
        }

    }

    private fun bindData() {

        graph = Graph()
        graph.init(binding.chartView)
        observeData()
        setListeners()
        viewModel.loadCoinData(coinId!!)
        viewModel.refreshCoinHistory(coinId!!)


    }

    private fun setListeners() {

        binding.apply {

            ivStar.setOnClickListener {
                viewModel.updateFavourite(coinId!!)
            }


            graphView.tabLayout.apply {
                selectTab(getTabAt(2))
                addOnTabSelectedListener(TabSelection { key ->
                    viewModel.refreshCoinHistory(
                        coinId = coinId!!,
                        key = key)
                })
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
            ivSearchIcon.setOnClickListener {
                (activity as? HomeActivity)?.navigateToTab(R.id.searchScreen)

            }
        }
    }

    private fun observeData() {
        collectFlow(viewModel.coin(coinId!!)) {
            Log.d("TAG", "observeData: $it")
            handleResponse(it)
        }

        store.stateFlow.map {
            it.loading
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { loading ->
            binding.apply {
                graphView.apply {
                    progressBar4.isVisible = loading
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(it: CoinDetailUI) {
        val currencySymbol = AppConstant.currencySymbol(appPrefs)

        val list: MutableList<Float> = ArrayList()
        if (it.coinHistoryData.isNotEmpty())
            it.coinHistoryData.map {
                if (it.price != null)
                    list.add(it.price.toFloat())
            }

        binding.graphView.sparkview.apply {
            adapter = CustomSparkAdapter(list.toFloatArray().reversedArray())
        }

        bindCoinChangeData(it.change, binding.coinChange)
        bindLinksData(it)


        bindGraph(it)

        Log.d("TAG", "binding.coinChange: " + it.change)

        binding.apply {
            it.coinData?.apply {
                btcPrice = AppConstant.roundToNearDecimal(5, it.coinData.btcPrice)
                coinDetail = it.coinData
                price.apply {
                    binding.price =
                        currencySymbol + AppConstant.formatNumber(this.toDouble())

                }
                allTimeHigh.apply {
                    tvAthValue.text =
                        "$" + AppConstant.formatNumber(this.price.toDouble())
                }
            }
            val favouriteIcon =
                if (it.isFavourite) R.drawable.star_icon_filled else R.drawable.star_icon

            ivStar.setImageResource(favouriteIcon)
        }
    }

    private fun bindLinksData(change: CoinDetailUI) {
        binding.coinLinksLayout.removeAllViews()

        change.coinData?.links?.forEach {
            binding.coinLinksLayout.addView(DynamicLinks(this).invoke(it) { url ->
                openWebView(url)
            })
        }
    }

    private fun openWebView(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        requireActivity().startActivity(launchBrowser)
    }


    private fun bindGraph(it: CoinDetailUI) {
        binding.apply {
            if (it.coinData!=null) {
                val list=it.coinHistoryData
                list.reverse()
                graph.bindData(list,it)
            }
        }
    }


    private fun bindCoinChangeData(change: String, coinChange: TextView) {

        coinChange.apply {
            if (change.isEmpty()) {
                setCompoundDrawablesWithIntrinsicBounds(0,
                    0,
                    0,
                    0)

                background =
                    ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_grey)
                return
            }

            change.let { change ->
                when {
                    change.toDouble() == 0.0 -> {
                        setCompoundDrawablesWithIntrinsicBounds(0,
                            0,
                            0,
                            0)

                        background =
                            ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_grey)
                    }
                    change.contains("-") -> {
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop_down_24,
                            0,
                            0,
                            0)

                        background =
                            ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_dark_red)
                    }
                    else -> {
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop_up_24,
                            0,
                            0,
                            0)
                        background =
                            ContextCompat.getDrawable(App.instance,
                                R.drawable.round_12dp_dark_green)
                    }
                }

                text = change
            }


        }

    }

}
