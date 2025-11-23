package com.example.onboardingflow.otp

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberEntryScreen(
    phoneNumber: String,
    onPhoneChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    errorMessage: String?,
    onBack: () -> Unit = {}
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    // Slide-up animation starting from bottom
    var targetOffsetY by remember { mutableStateOf(1000f) }
    val offsetY by animateFloatAsState(
        targetValue = targetOffsetY,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "bottomSheetSlide"
    )

    // Backdrop fade animation
    var targetAlpha by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 300),
        label = "bottomSheetFade"
    )

    LaunchedEffect(Unit) {
        targetOffsetY = 0f
        targetAlpha = 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
    ) {
        // Backdrop overlay with fade animation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f * alpha))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = offsetY
                    this.alpha = alpha
                }
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF000926),
                                Color(0xFF001F52)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x1F007EEB),
                                Color(0x12007EEB),
                                Color(0x00007EEB),
                                Color(0x00007EEB)
                            ),
                            center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                            radius = 1200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = "Verify your phone number",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFFFFF),
                    lineHeight = 38.4.sp,
                    letterSpacing = (-0.64).sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We're verifying your number securely before any SMS is sent.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFFFFFF).copy(alpha = 0.72f),
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = "+91",
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier.width(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color(0xFFFFFFFF),
                            disabledBorderColor = Color(0xFFFFFFFF),
                            disabledContainerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFFFFFFFF)
                        )
                    )

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
                                color = Color(0xFFFFFFFF)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFFFFFFF),
                            unfocusedTextColor = Color(0xFFFFFFFF),
                            focusedBorderColor = Color(0xFF167CE3),
                            unfocusedBorderColor = Color(0xFFFFFFFF),
                            cursorColor = Color(0xFF167CE3),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
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

                Text(
                    text = "By continuing, you agree to our Terms & Conditions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFFFFFF).copy(alpha = 0.72f),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        onSendOtp()
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
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
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
    }
}

@Composable
fun OtpEntryScreen(
    phoneNumber: String,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    timerSeconds: Int,
    canResend: Boolean,
    errorMessage: String?,
    hasInvalidOtp: Boolean = false,
    onBack: () -> Unit = {}
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    val offsetY by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "bottomSheetSlide"
    )

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "bottomSheetFade"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (otp.length < 6 && !hasInvalidOtp) 0.8f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "otpBoxGlow"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f * alpha))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = offsetY
                    this.alpha = alpha
                }
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF000926),
                                Color(0xFF001F52)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x1F007EEB),
                                Color(0x12007EEB),
                                Color(0x00007EEB),
                                Color(0x00007EEB)
                            ),
                            center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                            radius = 1200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                Text(
                    text = "We sent a code to +91 $phoneNumber",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFFFFFF).copy(alpha = 0.72f),
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

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
                                        if (isInvalid) Color(0xFFFFEBEC)
                                        else Color.White.copy(alpha = 0.1f)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = when {
                                            isInvalid -> Color(0xFFFF6B6B)
                                            isFilled -> Color.White
                                            isCurrent -> Color(0xFF167CE3).copy(alpha = glowAlpha.coerceAtLeast(1f))
                                            else -> Color.White.copy(alpha = 0.3f)
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
                                color = Color(0xFF00B7FF),
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
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
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
    }
}

@Composable
fun VerifyingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000926),
                        Color(0xFF001F52)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1F007EEB),
                            Color(0x12007EEB),
                            Color(0x00007EEB)
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = Color(0xFF00ACFE),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Verifying OTP...",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SuccessScreen(
    onComplete: () -> Unit = {}
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500),
        label = "successFade"
    )

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "successScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000926),
                        Color(0xFF001F52)
                    )
                )
            )
            .graphicsLayer { this.alpha = alpha },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1F007EEB),
                            Color(0x12007EEB),
                            Color(0x00007EEB)
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                        radius = 800f
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        onComplete()
    }
}

