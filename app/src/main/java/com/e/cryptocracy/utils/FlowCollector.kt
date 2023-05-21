package com.e.cryptocracy.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


fun <T> Fragment.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectFlow(flow: Flow<T>, collect: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}


fun <T> AppCompatActivity.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(collect)
    }
}

fun <T> AppCompatActivity.collectFlow(flow: Flow<T>, collect: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collect(collect)
    }
}
