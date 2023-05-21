package com.e.cryptocracy.home.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.cryptocracy.R
import com.e.cryptocracy.databinding.FragmentSearchScreenBinding
import com.e.cryptocracy.home.controller.search.SearchCoinAndMarketEpoxyController
import com.e.cryptocracy.home.viewModel.SearchViewModel
import com.e.cryptocracy.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchScreen : Fragment() {

    lateinit var binding: FragmentSearchScreenBinding
    lateinit var viewModel: SearchViewModel
    lateinit var controller: SearchCoinAndMarketEpoxyController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        controller = SearchCoinAndMarketEpoxyController(this)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchCoinMarketEpoxy.setController(controller)
        observers()
        setListeners()
    }

    private fun observers() {
        collectFlow(viewModel.searchResult) {
            binding.apply {
                noData.root.isVisible = it.error != null
                progressBar.root.isVisible = it.loading
                searchCoinMarketEpoxy.isInvisible = it.error != null
                noData.textView3.text = it.error
                noData.btnRetry.text = requireActivity().resources.getString(R.string.reset_search)
            }




            controller.setData(it)
        }
    }

    private fun setListeners() {
        binding.apply {
            btnCancel.setOnClickListener {
                if (editTextSearch.text.toString().isNotEmpty())
                    defaultSearch()
            }
            editTextSearch.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    when (s.length > 2) {
                        true -> {
                            viewModel.onQueryChange(s.toString())
                        }
                        false -> Log.d("TAG", "onViewCreated: ")
                    }
                }
            })

            noData.btnRetry.setOnClickListener {
                defaultSearch()
            }
        }

    }

    private fun defaultSearch() {
        viewModel.onQueryChange("")
        binding.editTextSearch.setText("")
    }

}