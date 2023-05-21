package com.e.cryptocracy.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.databinding.FragmentLoginScreenBinding
import com.e.cryptocracy.firebaseDatabase.FetchFavouriteCoinsFromServer
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.showToast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginScreen : Fragment() {

    lateinit var binding: FragmentLoginScreenBinding

    @Inject
    lateinit var store: Store<ApplicationState>
    private lateinit var updateFavIdsViewModel: FetchFavouriteCoinsFromServer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater)
        updateFavIdsViewModel = ViewModelProvider(this)[FetchFavouriteCoinsFromServer::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogin.setOnClickListener {
                showLoginDialog()
            }

            btnClose.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun showLoginDialog() {
        signInLauncher.launch(signInIntent)

    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }


    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    // Create and launch sign-in intent
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            updateFavIdsViewModel.fetch(user) {
                if (it) {
                    showToast("Logged In Successfully !!")
                    findNavController().navigateUp()
                }
            }

            /* lifecycleScope.launchWhenStarted {
                 store.update {
                     return@update it.copy(
                         userLoggedIn = true
                     )
                 }
             }*/


        } else {
            Snackbar.make(binding.root, "Failed to Login", Snackbar.LENGTH_SHORT).show()
        }
    }


}