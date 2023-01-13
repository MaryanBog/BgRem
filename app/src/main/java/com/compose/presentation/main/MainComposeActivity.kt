package com.compose.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bgrem.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_compose)
    }
}