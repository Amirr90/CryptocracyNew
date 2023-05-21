package com.e.cryptocracy.home.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.FragmentMarketSettingBinding
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppPrefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MarketSettingFragment : Fragment() {
    private lateinit var binding: FragmentMarketSettingBinding

    @Inject
    lateinit var appPrefs: AppPrefs

    @Inject
    lateinit var store: Store<ApplicationState>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMarketSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            marketSettingHeader.textView7.text = "Coin Setting"
            showPriceInBtcSwitch.isChecked = appPrefs.showPriceInBtc()
            switchGraph.isChecked = appPrefs.getShowGraph()
        }



        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            showPriceInBtcSwitch.setOnCheckedChangeListener { _, isChecked ->
                appPrefs.savePriceInBtcValue(isChecked)
                lifecycleScope.launchWhenStarted {
                    store.update {
                        return@update it.copy(
                            showPriceInBtc = isChecked
                        )
                    }
                }
            }

            switchGraph.setOnCheckedChangeListener { _, isChecked ->
                appPrefs.saveShowGraph(isChecked)
                lifecycleScope.launchWhenStarted {
                    store.update {
                        return@update it.copy(
                            showGraph = isChecked
                        )
                    }
                }
            }
        }
    }
}