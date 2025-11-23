package com.example.onboardingflow.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onboardingflow.data.UserPreferencesRepository

class OtpViewModelFactory(
    private val repository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OtpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

