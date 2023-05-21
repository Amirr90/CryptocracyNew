package com.e.cryptocracy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.e.cryptocracy.databinding.ActivityMainBinding
import com.e.cryptocracy.firebaseDatabase.FetchFavouriteCoinsFromServer
import com.e.cryptocracy.home.ui.HomeActivity
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    @Inject
    lateinit var store: Store<ApplicationState>

    lateinit var binding: ActivityMainBinding

    private lateinit var updateFavIdsViewModel: FetchFavouriteCoinsFromServer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateFavIdsViewModel = ViewModelProvider(this)[FetchFavouriteCoinsFromServer::class.java]

    }


    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            //navigateToHomeScreen()
            handleUser(auth.currentUser)
        }


    }

    private fun handleUser(user: FirebaseUser?) {
        when (user) {
            null -> {
                navigateToHomeScreen()
            }
            else -> {
                updateFavIdsViewModel.fetch(user) {
                    navigateToHomeScreen()
                }

            }
        }
    }


    private fun navigateToHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }


}