package com.example.onboardingflow.otp

data class OtpUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val isOtpSent: Boolean = false,
    val errorMessage: String? = null,
    val isVerifying: Boolean = false,
    val timerSeconds: Int = 60,
    val canResend: Boolean = false,
    val hasInvalidOtp: Boolean = false
)

