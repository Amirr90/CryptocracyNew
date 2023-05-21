package com.e.cryptocracy.globalStats.dynamicView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.e.cryptocracy.databinding.CoinWithPriceViewBinding
import com.e.cryptocracy.globalStats.dto.NewestCoin
import com.e.cryptocracy.utils.navigateToCoinDetailScreen

class ViewForNewCoin(
    val activity: Fragment,
) {

    val inflater = activity.requireActivity()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var view: CoinWithPriceViewBinding

    fun invoke(newestCoin: NewestCoin, counter: String): View {
        view = CoinWithPriceViewBinding.inflate(inflater)


        view.apply {
            textView4.text = counter
            textView2.text = newestCoin.symbol
            coinPrice.text = newestCoin.name
            image = newestCoin.iconUrl
            card.setOnClickListener {
                activity.navigateToCoinDetailScreen(newestCoin.uuid)
            }
        }
        return view.root
    }
}