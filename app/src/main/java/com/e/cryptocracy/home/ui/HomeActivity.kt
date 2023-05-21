package com.e.cryptocracy.home.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.ActivityHomeBinding
import com.e.cryptocracy.home.models.FavouriteUi
import com.e.cryptocracy.login.loginUtils.LoginUtils
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val idsToHideBottomBar = listOf(
        R.id.coinDetailScreen,
        R.id.loginScreen,
        R.id.marketSettingFragment,
    )

    private val idsToHideToolbar = listOf(
        R.id.coinDetailScreen,
        R.id.loginScreen,
    )


    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var loginUtils: LoginUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.coinListingScreen,
                R.id.accountScreen,
                R.id.favouriteScreen,
                R.id.exchangeScreen,
                R.id.searchScreen,
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.isVisible = !idsToHideToolbar.contains(destination.id)
            binding.bottomNavigationCard.isVisible = !idsToHideBottomBar.contains(destination.id)

        }

        setUpFavouriteCount()
    }

    private fun setUpFavouriteCount() {
        combine(store.stateFlow.map {
            it.favouriteCoinIds.size
        }, store.stateFlow.map {
            it.userLoggedIn
        }) { favouriteCoinIds, userLoggedInStatus ->
            FavouriteUi(
                favouriteListCounter = favouriteCoinIds,
                loggedIn = userLoggedInStatus
            )
        }.distinctUntilChanged().asLiveData().observe(this) {
            Log.d("TAG", "setUpFavouriteCount: $it")
            if (it.loggedIn) {
                binding.bottomNavigation.getOrCreateBadge(R.id.favouriteScreen).apply {
                    number = it.favouriteListCounter
                    isVisible = it.favouriteListCounter > 0
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    fun navigateToTab(@IdRes destinationId: Int) {
        binding.bottomNavigation.selectedItemId = destinationId
    }
}