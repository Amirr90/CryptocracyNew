package com.e.cryptocracy.utils.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.lang.String.format
import java.text.DecimalFormat

object UtilsBinding {

    @JvmStatic
    @BindingAdapter("bindValueInRoman")
    fun bindValueInRoman(tv: TextView, number: String?) {
        number?.apply {
            if (number == "null") {
                tv.text = "----------"
                return
            }

            var value = number.toFloat()
            val arr = arrayOf("", "K", "M", "Bn", "Tn", "P", "E")
            var index = 0
            while (value / 1000 >= 1) {
                value /= 1000
                index++
            }
            val decimalFormat = DecimalFormat("#.##")
            val res = java.lang.String.format("%s %s", decimalFormat.format(value), arr[index])
            tv.text = res
        }

    }

}