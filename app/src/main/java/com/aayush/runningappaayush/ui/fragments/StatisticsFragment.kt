package com.aayush.runningappaayush.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aayush.runningappaayush.R
import com.aayush.runningappaayush.ui.MainViewModel
import com.aayush.runningappaayush.ui.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment:Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()
}