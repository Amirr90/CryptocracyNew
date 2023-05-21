package com.e.cryptocracy.auth

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAppAuth(
    activity: AppCompatActivity,
) {
    fun invoke() {
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = activity.registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
    )

    // Create and launch sign-in intent
    private val signInIntent =
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Log.d("TAG", "onSignInResult: Success ${user?.metadata}")
        } else {
            Log.d("TAG", "onSignInResult: Failed")
        }
    }
}