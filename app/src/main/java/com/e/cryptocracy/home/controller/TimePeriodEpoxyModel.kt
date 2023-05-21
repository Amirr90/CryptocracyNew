package com.e.cryptocracy.home.controller

import android.util.Log
import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.TimePeriodViewBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel
import com.e.cryptocracy.home.models.TimePeriod
import com.e.cryptocracy.home.models.TimePeriodUi

data class TimePeriodEpoxyModel(
    val periodData: TimePeriodUi,
    val clickListener: (periodData: TimePeriod) -> Unit,
) : ViewBindingKotlinModel<TimePeriodViewBinding>(R.layout.time_period_view) {
    override fun TimePeriodViewBinding.bind() {
        timePeriod = periodData.timePeriodData
        mainTimePeriodView.setOnClickListener {
            clickListener(periodData.timePeriodData)
        }

        mainTimePeriodView.isChecked = periodData.selected

    }
}