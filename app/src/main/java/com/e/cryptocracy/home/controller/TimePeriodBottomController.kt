package com.e.cryptocracy.home.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.e.cryptocracy.home.models.TimePeriodUi
import java.util.*

class TimePeriodBottomController(
    val onTimePeriodClick: (id: String) -> Unit,
) :
    TypedEpoxyController<List<TimePeriodUi>>() {
    override fun buildModels(data: List<TimePeriodUi>?) {

        if (data.isNullOrEmpty()) {
            return
        }

        data.forEach {
            //val epoxyId = UUID.randomUUID().toString()
            TimePeriodEpoxyModel(it, clickListener = { timePeriodId ->
                onTimePeriodClick(timePeriodId.value)
            }).id(it.timePeriodData.value).addTo(this)
        }


    }
}