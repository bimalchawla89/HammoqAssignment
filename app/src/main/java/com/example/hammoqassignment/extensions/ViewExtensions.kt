package com.example.hammoqassignment.extensions

import android.view.View

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}