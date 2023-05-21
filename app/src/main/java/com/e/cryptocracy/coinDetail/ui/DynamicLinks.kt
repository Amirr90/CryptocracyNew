package com.e.cryptocracy.coinDetail.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.e.cryptocracy.coinDetail.dto.Link
import com.e.cryptocracy.databinding.CoinLinksViewBinding


class DynamicLinks(
    val activity: Fragment,
) {

    val inflater = activity.requireActivity()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    lateinit var view: CoinLinksViewBinding

    fun invoke(link: Link, action: (url: String) -> Unit): View {
        view = CoinLinksViewBinding.inflate(inflater)
        view.apply {
            links = link
            mainLinkView.setOnClickListener {
                action(link.url)
            }
        }
        return view.root
    }


}