package com.example.onboardingflow.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onboardingflow.data.UserPreferencesRepository

@Composable
fun OtpScreen(
    onVerified: () -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { UserPreferencesRepository(context) }
    val viewModel: OtpViewModel = viewModel(factory = OtpViewModelFactory(repository))
    val uiState by viewModel.uiState.collectAsState()

    when {
        !uiState.isOtpSent -> {
            PhoneNumberEntryScreen(
                phoneNumber = uiState.phoneNumber,
                onPhoneChanged = viewModel::onPhoneChanged,
                onSendOtp = viewModel::sendOtp,
                errorMessage = uiState.errorMessage,
                onBack = onBack
            )
        }
        uiState.isVerifying -> {
            VerifyingScreen()
        }
        else -> {
            OtpEntryScreen(
                phoneNumber = uiState.phoneNumber,
                otp = uiState.otp,
                onOtpChanged = viewModel::onOtpChanged,
                onVerify = { viewModel.verifyOtp(onVerified) },
                onResend = viewModel::resendOtp,
                timerSeconds = uiState.timerSeconds,
                canResend = uiState.canResend,
                errorMessage = uiState.errorMessage,
                hasInvalidOtp = uiState.hasInvalidOtp,
                onBack = { viewModel.resetToPhoneEntry() }
            )
        }
    }
}

