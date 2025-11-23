package com.example.onboardingflow.sheet

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ====================================
// PHONE NUMBER SHEET CONTENT
// ====================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberSheetContent(
    phoneNumber: String,
    onPhoneChanged: (String) -> Unit,
    onContinue: () -> Unit,
    onClose: () -> Unit,
    errorMessage: String? = null
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Close icon (top-right X)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Title - Switzer 32px Semibold -2% letter spacing 120% line height
        Text(
            text = "Verify your phone number",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold, // 600
            color = Color(0xFFFFFFFF),
            lineHeight = 38.4.sp, // 120% of 32sp
            letterSpacing = (-0.64).sp, // -2% of 32sp
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle - 12px Regular 150% line height 72% opacity
        Text(
            text = "We're verifying your number securely before any SMS is sent.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFFFFFFF).copy(alpha = 0.72f), // 72% opacity
            lineHeight = 18.sp, // 150% of 12sp
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Phone input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Country code box (locked)
            OutlinedTextField(
                value = "+91",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.width(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color(0xFFFFFFFF),
                    disabledBorderColor = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                    disabledContainerColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFFFFFF)
                )
            )

            // Phone number field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                        onPhoneChanged(newValue)
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Enter Phone Number",
                        fontSize = 14.sp,
                        color = Color(0xFFFFFFFF).copy(alpha = 0.5f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF),
                    focusedBorderColor = Color(0xFF167CE3), // Blue when focused
                    unfocusedBorderColor = Color(0xFFFFFFFF).copy(alpha = 0.5f), // White unfocused
                    cursorColor = Color(0xFF167CE3),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorBorderColor = Color(0xFFFF6B6B),
                    errorContainerColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFFFFFF)
                ),
                isError = errorMessage != null
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Terms & Conditions - 12sp centered underline 72% opacity
        Text(
            text = "By continuing, you agree to our Terms & Conditions",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFFFFFFF).copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            lineHeight = 18.sp, // 150%
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button - Exact Figma dual gradient
        Button(
            onClick = {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                onContinue()
            },
            enabled = phoneNumber.length == 10,
            modifier = Modifier
                .width(312.dp)
                .height(48.dp)
                .align(Alignment.CenterHorizontally)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFFFFFFF).copy(alpha = 0.3f)
            ),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> isPressed = true
                                is PressInteraction.Release -> isPressed = false
                                is PressInteraction.Cancel -> isPressed = false
                            }
                        }
                    }
                }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Layer 1: Base gradient linear-gradient(0deg, #007EEB, #007EEB)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = Color(0xFF007EEB),
                            shape = RoundedCornerShape(56.dp)
                        )
                )

                // Layer 2: Radial gradient overlay
                // radial-gradient(50% 100% at 50% 100%, #00ACFE 0%, rgba(0,172,254,0) 100%)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00ACFE),
                                    Color(0x0000ACFE)
                                ),
                                center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                                radius = 500f
                            ),
                            shape = RoundedCornerShape(56.dp)
                        )
                )

                // Button text
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ====================================
// OTP SHEET CONTENT
// ====================================
@Composable
fun OtpSheetContent(
    phoneNumber: String,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit,
    timerSeconds: Int,
    canResend: Boolean,
    errorMessage: String? = null,
    hasInvalidOtp: Boolean = false
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    // OTP box glow animation (pulsing)
    val glowAlpha by animateFloatAsState(
        targetValue = if (otp.length < 6 && !hasInvalidOtp) 0.8f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "otpBoxGlow"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Back arrow (top-left)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = onBack) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }

        // Title
        Text(
            text = "Enter the OTP sent to your Phone",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFFFFFF),
            lineHeight = 28.8.sp,
            letterSpacing = (-0.48).sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "We sent a code to +91 $phoneNumber",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFFFFFFF).copy(alpha = 0.72f),
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // OTP boxes - Six boxes 45.33 × 48 dp
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                for (i in 0 until 6) {
                    val isFilled = i < otp.length
                    val isCurrent = i == otp.length
                    val isInvalid = hasInvalidOtp

                    Box(
                        modifier = Modifier
                            .width(45.33.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isInvalid) Color(0xFFFFEBEC) // Light red background
                                else Color.White.copy(alpha = 0.1f)
                            )
                            .border(
                                width = 1.dp,
                                color = when {
                                    isInvalid -> Color(0xFFFF6B6B) // Red border
                                    isFilled -> Color.White // Filled: white
                                    isCurrent -> Color(0xFF167CE3).copy(alpha = glowAlpha.coerceAtLeast(1f)) // Current: blue glow
                                    else -> Color.White.copy(alpha = 0.3f) // Empty: white 30%
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = otp.getOrNull(i)?.toString() ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isInvalid) Color(0xFFFF6B6B) else Color.White,
                            lineHeight = 22.4.sp,
                            letterSpacing = (-0.32).sp
                        )
                    }
                }
            }

            // Invisible BasicTextField for input with haptic feedback
            BasicTextField(
                value = otp,
                onValueChange = { newValue ->
                    if (newValue.length <= 6 && (newValue.isEmpty() || newValue.all { it.isDigit() })) {
                        if (newValue != otp) {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        }
                        onOtpChanged(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                cursorBrush = SolidColor(Color.Transparent),
                decorationBox = { }
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend section with blue underline
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (canResend) {
                TextButton(onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    onResend()
                }) {
                    Text(
                        "Didn't receive the code? ",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 19.6.sp
                    )
                    Text(
                        "Resend Code",
                        color = Color(0xFF00B7FF), // Blue
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 19.6.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            } else {
                Text(
                    text = "Resend OTP in ${timerSeconds}s",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 19.6.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Verify Button
        Button(
            onClick = {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                onVerify()
            },
            enabled = otp.length == 6,
            modifier = Modifier
                .width(312.dp)
                .height(48.dp)
                .align(Alignment.CenterHorizontally)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFFFFFFF).copy(alpha = 0.3f)
            ),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> isPressed = true
                                is PressInteraction.Release -> isPressed = false
                                is PressInteraction.Cancel -> isPressed = false
                            }
                        }
                    }
                }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = Color(0xFF007EEB),
                            shape = RoundedCornerShape(56.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00ACFE),
                                    Color(0x0000ACFE)
                                ),
                                center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                                radius = 500f
                            ),
                            shape = RoundedCornerShape(56.dp)
                        )
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Verify my number",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ====================================
// SUCCESS SHEET CONTENT
// ====================================
@Composable
fun SuccessSheetContent(
    onComplete: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "successScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Glowing checkmark circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00ACFE),
                            Color(0xFF007EEB)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Phone Verified Successfully!",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your phone number has been verified",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }

    // Auto-navigate back after 2 seconds
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        onComplete()
    }
}

