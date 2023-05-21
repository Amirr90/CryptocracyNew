package com.e.cryptocracy.home.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.FragmentFavouriteScreenBinding
import com.e.cryptocracy.home.controller.MarketControllerNonPaging
import com.e.cryptocracy.home.ui.HomeActivity
import com.e.cryptocracy.home.viewModel.HomeViewModel
import com.e.cryptocracy.login.loginUtils.LoginUtils
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class FavouriteScreen : Fragment() {
    private lateinit var controller: MarketControllerNonPaging
    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var appPrefs: AppPrefs

    @Inject
    lateinit var loginUtils: LoginUtils
    lateinit var binding: FragmentFavouriteScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavouriteScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        controller = MarketControllerNonPaging(appPrefs, requireParentFragment())
        binding.favouriteCoinListingEpoxy.setController(controller)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginUtils.handleUserLoggedInNavigation(this)

        collectFlow(viewModel.favouriteCoins()) {
            controller.setData(it.coins)

            binding.apply {
                progressBar2.isVisible = it.loading
                errorLayout.isVisible = it.error != null
                favouriteCoinListingEpoxy.isInvisible = it.error != null
            }
        }
        store.stateFlow.map {
            it.favouriteCoinIds
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) {
            when {
                it.isNotEmpty() -> {
                    viewModel.loadFavouriteCoins(it.toList())
                }
            }


            binding.apply {
                noFavLayout.isVisible = it.isEmpty()
                favouriteCoinListingEpoxy.isVisible = it.isNotEmpty()
            }


        }

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            favLayout.btnGoToCoinListingScreen.setOnClickListener {
                (activity as? HomeActivity)?.navigateToTab(R.id.coinListingScreen)
            }
        }
    }

}