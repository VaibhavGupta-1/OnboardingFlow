package com.example.onboardingflow.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onboardingflow.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {
    val hasCompletedOnboarding: StateFlow<Boolean> = repository.onboardingCompleted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            repository.setOnboardingCompleted(true)
        }
    }
}
