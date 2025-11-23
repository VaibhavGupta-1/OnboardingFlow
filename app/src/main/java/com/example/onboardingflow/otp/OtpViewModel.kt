package com.example.onboardingflow.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onboardingflow.data.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState

    fun onPhoneChanged(value: String) {
        if (value.length <= 10 && value.all { it.isDigit() }) {
            _uiState.update { it.copy(phoneNumber = value, errorMessage = null) }
        }
    }

    fun sendOtp() {
        if (_uiState.value.phoneNumber.length < 10) {
            _uiState.update { it.copy(errorMessage = "Enter valid 10-digit phone number") }
            return
        }
        viewModelScope.launch {
            repository.savePhoneNumber(_uiState.value.phoneNumber)
            _uiState.update { it.copy(isOtpSent = true, errorMessage = null, timerSeconds = 60, canResend = false) }
            startTimer()
        }
    }

    fun onOtpChanged(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) {
            _uiState.update { it.copy(otp = value, errorMessage = null, hasInvalidOtp = false) }
        }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val otp = _uiState.value.otp
        if (otp.length < 6) {
            _uiState.update { it.copy(errorMessage = "Please enter complete 6-digit OTP") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isVerifying = true, errorMessage = null) }
            delay(1500)
            // Mock verification - only accept 123456 for wrong OTP demo
            if (otp == "123456") {
                _uiState.update { it.copy(isVerifying = false, errorMessage = null, hasInvalidOtp = false) }
                onSuccess()
            } else {
                // Don't reset timer or OTP sent state, just show error
                _uiState.update { it.copy(
                    isVerifying = false,
                    errorMessage = "Invalid OTP. Please try again.",
                    hasInvalidOtp = true,
                    otp = "" // Clear OTP for re-entry
                ) }
            }
        }
    }

    fun resendOtp() {
        if (!_uiState.value.canResend) return
        _uiState.update { it.copy(timerSeconds = 60, canResend = false, otp = "", errorMessage = null, hasInvalidOtp = false) }
        startTimer()
    }

    fun resetToPhoneEntry() {
        _uiState.update { OtpUiState(phoneNumber = _uiState.value.phoneNumber) }
    }

    private fun startTimer() {
        viewModelScope.launch {
            for (seconds in 59 downTo 0) {
                delay(1000)
                _uiState.update { it.copy(timerSeconds = seconds) }
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }
}
