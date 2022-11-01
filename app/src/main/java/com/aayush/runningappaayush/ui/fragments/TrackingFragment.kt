package com.aayush.runningappaayush.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aayush.runningappaayush.R
import com.aayush.runningappaayush.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment:Fragment(R.layout.fragment_tracking) {
    private val viewModel: MainViewModel by viewModels()
}