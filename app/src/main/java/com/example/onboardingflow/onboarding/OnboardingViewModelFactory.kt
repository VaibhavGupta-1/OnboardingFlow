package com.example.onboardingflow.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onboardingflow.data.UserPreferencesRepository

class OnboardingViewModelFactory(
    private val repository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

