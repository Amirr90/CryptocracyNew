package com.e.cryptocracy.home.controller

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.e.cryptocracy.R
import com.e.cryptocracy.auth.App
import com.e.cryptocracy.databinding.CoinLayoutBinding
import com.e.cryptocracy.epoxy.ViewBindingKotlinModel
import com.e.cryptocracy.home.models.Coin
import com.e.cryptocracy.home.spark.CustomSparkAdapter

data class CoinEpoxyModel(
    val data: Coin?,
    val currency_Logo: String,
    val onCardClick: (Coin?) -> Unit,
    val showBtcPrice: Boolean,
    val showGraph: Boolean,
) : ViewBindingKotlinModel<CoinLayoutBinding>(R.layout.coin_layout) {

    @SuppressLint("ResourceAsColor")
    override fun CoinLayoutBinding.bind() {
        card.isInvisible = data == null
        currencyLogo = currency_Logo
        data?.apply {
            coin = data

            sparkview.isScrubEnabled = true
            sparkview.adapter = CustomSparkAdapter(sparkLine)

            card.setOnClickListener {
                onCardClick(this)
            }
            coinChange.apply {
                change?.apply {
                    when {
                        this.contains("-") -> {
                            sparkview.lineColor = getColor(App.instance, R.color.dark_red)
                            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop_down_24,
                                0,
                                0,
                                0)

                            background =
                                ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_red)
                            setTextColor(App.instance.resources.getColor(R.color.red_text))
                        }
                        else -> {
                            sparkview.lineColor = getColor(App.instance, R.color.dark_green)
                            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop_up_24,
                                0,
                                0,
                                0)
                            background =
                                ContextCompat.getDrawable(App.instance, R.drawable.round_12dp_green)
                            setTextColor(App.instance.resources.getColor(R.color.green_text))
                        }
                    }
                }
            }

            var color = R.color.white
            try {
                color = Color.parseColor(data.color)
            } catch (ex: Exception) {
                Log.d("TAG", "bind: ${ex.localizedMessage}")
            }

            view.backgroundTintList = ColorStateList.valueOf(color)

            tvBtcPrice.isVisible = showBtcPrice
            sparkview.isVisible = showGraph

        }
    }

}
