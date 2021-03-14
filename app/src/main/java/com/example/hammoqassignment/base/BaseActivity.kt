package com.example.hammoqassignment.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity : AppCompatActivity() {

    protected open val binding: ViewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Update the view from binding
        binding?.root?.let {
            setContentView(it)
        }
    }
}