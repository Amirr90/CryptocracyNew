package com.e.cryptocracy.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.e.cryptocracy.databinding.FragmentTimePeriodBottomDialogBinding
import com.e.cryptocracy.home.controller.TimePeriodBottomController
import com.e.cryptocracy.home.models.TimePeriod
import com.e.cryptocracy.home.models.TimePeriodUi
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppPrefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class TimePeriodBottomDialogFragment : BottomSheetDialogFragment() {

    lateinit var controller: TimePeriodBottomController
    private lateinit var binding: FragmentTimePeriodBottomDialogBinding

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var appPrefs: AppPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTimePeriodBottomDialogBinding.inflate(layoutInflater)
        controller = TimePeriodBottomController { id ->
            lifecycleScope.launchWhenStarted {
                appPrefs.saveValue(key = AppPrefs.TIME_PERIOD, value = id)
                store.update {
                    return@update it.copy(
                        selectedTimePeriodId = id
                    )
                }
            }
        }

        binding.timePeriodEpoxy.setController(controller)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            store.update {
                return@update it.copy(
                    timePeriodData = listOf(
                        "1h", "3h", "12h", "24h",
                    )
                )
            }
        }


        combine(
            store.stateFlow.map { it.timePeriodData },
            store.stateFlow.map { it.selectedTimePeriodId }
        ) { currencyList, selectedId ->
            currencyList.map {
                TimePeriodUi(
                    timePeriodData = TimePeriod(value = it),
                    selected = selectedId.contains(it)
                )
            }
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) {
            controller.setData(it)

        }

    }
}