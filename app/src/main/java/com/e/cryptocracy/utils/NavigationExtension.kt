package com.e.cryptocracy.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.R

fun Fragment.navigateToCoinDetailScreen(coinId: String) {
    val bundle = Bundle()
    bundle.putString("coinId", coinId)
    findNavController().navigate(R.id.action_global_coinDetailScreen, bundle)
}

fun Fragment.navigateToLoginScreen(){
    findNavController().navigate(R.id.action_global_loginScreen)
}