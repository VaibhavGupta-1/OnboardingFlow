package com.example.onboardingflow.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ====================================
// SHEET ROUTE DEFINITION
// ====================================
sealed class SheetRoute {
    object Phone : SheetRoute()
    object Otp : SheetRoute()
    object Success : SheetRoute()
}

// ====================================
// ONBOARDING HOST WITH SHEET ROUTING
// ====================================
@Composable
fun OnboardingHost(
    sheetVisible: Boolean,
    onDismissSheet: () -> Unit,
    iUnderstandScreen: @Composable (onShowSheet: () -> Unit) -> Unit
) {
    var currentRoute by remember { mutableStateOf<SheetRoute>(SheetRoute.Phone) }

    // Phone state
    var phoneNumber by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }

    // OTP state
    var otp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var hasInvalidOtp by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Timer for OTP resend
    LaunchedEffect(currentRoute) {
        if (currentRoute == SheetRoute.Otp && !canResend) {
            while (timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
            canResend = true
        }
    }

    // Reset invalid OTP state after 1.2 seconds
    LaunchedEffect(hasInvalidOtp) {
        if (hasInvalidOtp) {
            delay(1200)
            hasInvalidOtp = false
        }
    }

    // Base screen: "I Understand" screen
    Box {
        iUnderstandScreen(onDismissSheet)

        // Sliding blue sheet overlay
        SlidingBlueSheet(
            visible = sheetVisible,
            onDismiss = {
                onDismissSheet()
                // Reset state when sheet closes
                currentRoute = SheetRoute.Phone
                phoneNumber = ""
                otp = ""
                phoneError = null
                otpError = null
                hasInvalidOtp = false
                timerSeconds = 60
                canResend = false
            }
        ) {
            when (currentRoute) {
                SheetRoute.Phone -> {
                    PhoneNumberSheetContent(
                        phoneNumber = phoneNumber,
                        onPhoneChanged = {
                            phoneNumber = it
                            phoneError = null
                        },
                        onContinue = {
                            if (phoneNumber.length == 10) {
                                currentRoute = SheetRoute.Otp
                                otp = ""
                                timerSeconds = 60
                                canResend = false
                            } else {
                                phoneError = "Please enter a valid 10-digit phone number"
                            }
                        },
                        onClose = {
                            onDismissSheet()
                            phoneNumber = ""
                            phoneError = null
                        },
                        errorMessage = phoneError
                    )
                }

                SheetRoute.Otp -> {
                    OtpSheetContent(
                        phoneNumber = phoneNumber,
                        otp = otp,
                        onOtpChanged = {
                            otp = it
                            otpError = null
                            hasInvalidOtp = false
                        },
                        onVerify = {
                            if (otp.length == 6) {
                                // Fake verification with 1.5s delay
                                coroutineScope.launch {
                                    delay(1500)

                                    // Mock validation: "123456" is valid, others invalid
                                    if (otp == "123456") {
                                        currentRoute = SheetRoute.Success
                                    } else {
                                        hasInvalidOtp = true
                                        otpError = "Invalid OTP. Please try again."
                                    }
                                }
                            }
                        },
                        onResend = {
                            otp = ""
                            timerSeconds = 60
                            canResend = false
                            otpError = null
                            hasInvalidOtp = false
                        },
                        onBack = {
                            currentRoute = SheetRoute.Phone
                            otp = ""
                            otpError = null
                            hasInvalidOtp = false
                        },
                        timerSeconds = timerSeconds,
                        canResend = canResend,
                        errorMessage = otpError,
                        hasInvalidOtp = hasInvalidOtp
                    )
                }

                SheetRoute.Success -> {
                    SuccessSheetContent(
                        onComplete = {
                            // Auto-dismiss sheet after 2 seconds
                            onDismissSheet()
                            currentRoute = SheetRoute.Phone
                            phoneNumber = ""
                            otp = ""
                            phoneError = null
                            otpError = null
                        }
                    )
                }
            }
        }
    }
}

