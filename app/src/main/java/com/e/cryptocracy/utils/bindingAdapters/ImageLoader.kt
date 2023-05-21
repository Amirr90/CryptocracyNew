package com.e.cryptocracy.utils.bindingAdapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.e.cryptocracy.R
import com.e.cryptocracy.auth.App

object ImageLoader {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(image: ImageView, url: String?) {
        url?.apply {
            Glide
                .with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .disallowHardwareConfig()
                .into(image);

        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(image: ImageView, url: Uri?) {
        url?.apply {
            Glide
                .with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .disallowHardwareConfig()
                .into(image);

        }
    }
}