package com.aayush.runningappaayush.ui


import androidx.lifecycle.ViewModel
import com.aayush.runningappaayush.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
):ViewModel(){



}