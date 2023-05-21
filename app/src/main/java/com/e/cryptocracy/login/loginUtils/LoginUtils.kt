package com.e.cryptocracy.login.loginUtils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.R
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.navigateToLoginScreen
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginUtils @Inject constructor(
    val store: Store<ApplicationState>,
) {
    val isLogin = when (FirebaseAuth.getInstance().currentUser) {
        null -> false
        else -> {
            true
        }
    }

    fun handleUserLoggedInNavigation(fragment: Fragment) {

        when (FirebaseAuth.getInstance().currentUser) {
            null -> {
                fragment.navigateToLoginScreen()
            }
        }
    }

    fun handleUserLoggedInNavigation(navController: NavController) {
        when (FirebaseAuth.getInstance().currentUser) {
            null -> {
                navController.navigate(R.id.action_global_loginScreen)
            }
        }
    }

    fun logOut(fragment: Fragment, status: (value: Boolean) -> Unit) {
        AuthUI.getInstance()
            .signOut(fragment.requireActivity())
            .addOnCompleteListener {
                status(it.isSuccessful)
            }
    }
}
