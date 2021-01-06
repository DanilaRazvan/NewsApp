package com.bearddr.newsapp.util.extensions

import android.widget.ImageView
import com.bearddr.newsapp.R
import com.bumptech.glide.Glide

fun ImageView.loadFromUrl(url: String) {
    Glide
        .with(this)
        .load(url)
        .placeholder(R.drawable.image_placeholder)
        .into(this)
}