package com.e.cryptocracy.account.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.e.cryptocracy.account.controller.AccountEpoxyController
import com.e.cryptocracy.account.viewModel.AccountViewModel
import com.e.cryptocracy.databinding.FragmentAccountScreenBinding
import com.e.cryptocracy.home.ui.HomeActivity
import com.e.cryptocracy.login.loginUtils.LoginUtils
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.utils.AppConstant
import com.e.cryptocracy.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class AccountScreen : Fragment() {

    private lateinit var binding: FragmentAccountScreenBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var accountEpoxyController: AccountEpoxyController

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var loginUtils: LoginUtils
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountEpoxyController = AccountEpoxyController {
            onAccountItemClick(it)
        }
        binding.accountEpoxy.setController(accountEpoxyController)

        loginUtils.handleUserLoggedInNavigation(this)

        store.stateFlow.map {
            it.accountData
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) {
            accountEpoxyController.setData(it)
        }


        viewModel.refreshAccountData()

        setListeners()
        bindUserInformation()
    }

    @SuppressLint("SetTextI18n")
    private fun bindUserInformation() {
        user?.let { firebaseUser ->
            binding.apply {
                textView7.text = firebaseUser.displayName ?: "No_name"
                textView6.text = firebaseUser.email ?: "No_email"
                val date = firebaseUser.metadata?.lastSignInTimestamp
                date?.apply {
                    textView9.text =
                        "Last Logged in  ${AppConstant.dateFromTimestamp(this)}"
                }
                image = firebaseUser.photoUrl


            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnLogout.setOnClickListener {
                progressLayout.progressBar3.isVisible = true
                loginUtils.logOut(requireParentFragment()) {
                    lifecycleScope.launchWhenStarted {
                        store.update { storeUp ->
                            return@update storeUp.copy(
                                userLoggedIn = it,
                                favouriteCoinIds = emptySet(),
                                favouriteCoins = emptyList(),
                            )
                        }
                    }
                    progressLayout.progressBar3.isVisible = false
                    if (it) {
                        showToast("Logged Out Successfully !!")
                        requireActivity().startActivity(Intent(requireActivity(),
                            HomeActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }


    }

    private fun onAccountItemClick(id: Int) {
        when (id) {
            0 -> {
                showToast("Coming Soon !!")
            }
            else -> {
                findNavController().navigate(id)
            }
        }

    }
}