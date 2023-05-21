package com.e.cryptocracy.utils

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode


fun Fragment.showSnackBar(view: View, msg: String) {
    Snackbar.make(view,
        msg,
        Snackbar.LENGTH_LONG).show()
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.hidingToolbar() {
    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
}

fun Fragment.bindPrice(price: Double): BigDecimal {
    val scale = if (price.toInt() > 0) {
        2
    } else price.toString().split(".").last().find {
        it.toString() == "0"
    }.toString().length
    return BigDecimal(price).setScale(scale + 1, RoundingMode.HALF_UP)
}