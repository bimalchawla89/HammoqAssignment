package com.example.hammoqassignment.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.hammoqassignment.utils.CommonFunctions
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

@BindingAdapter("image")
fun ImageView.setImage(
    url: String?
) {
    url?.let {
        Picasso.get().load(it)
            .transform(RoundedCornersTransformation(12, 12)).into(this)
    }
}