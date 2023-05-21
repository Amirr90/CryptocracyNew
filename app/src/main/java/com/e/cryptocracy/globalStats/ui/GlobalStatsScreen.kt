package com.e.cryptocracy.globalStats.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.cryptocracy.databinding.FragmentGlobalStatsScreenBinding
import com.e.cryptocracy.globalStats.dynamicView.ViewForBestCoin
import com.e.cryptocracy.globalStats.dynamicView.ViewForNewCoin
import com.e.cryptocracy.globalStats.model.GlobalStatsUi
import com.e.cryptocracy.globalStats.viewModels.GlobalStatsViewModel
import com.e.cryptocracy.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GlobalStatsScreen : Fragment() {

    lateinit var binding: FragmentGlobalStatsScreenBinding
    lateinit var viewModel: GlobalStatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGlobalStatsScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GlobalStatsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeData()
    }

    private fun observeData() {
        collectFlow(viewModel.statsData()) {
            handleResponse(it)
        }
    }

    private fun handleResponse(it: GlobalStatsUi) {
        binding.apply {
            it.statsData?.apply {
                statsData = this
            }

            bindDynamicView(it)
            globalProgressView.root.isVisible = it.loading
        }

    }

    private fun bindDynamicView(it: GlobalStatsUi) {
        clearView()
        binding.apply {
            repeat(it.newCoins.size) { counter ->
                layoutNewestCoin.addView(ViewForNewCoin(this@GlobalStatsScreen).invoke(it.newCoins[counter],
                    (counter + 1).toString()))

            }
            repeat(it.bestCoin.size) { counter ->
                layoutBestCoin.addView(ViewForBestCoin(this@GlobalStatsScreen).invoke(it.bestCoin[counter],
                    (counter + 1).toString()))
            }
        }

    }

    private fun clearView() {
        binding.layoutNewestCoin.removeAllViews()
        binding.layoutBestCoin.removeAllViews()
    }
}