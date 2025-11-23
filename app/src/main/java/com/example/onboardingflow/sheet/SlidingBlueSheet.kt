package com.example.onboardingflow.sheet

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SlidingBlueSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val sheetMaxHeight = screenHeightPx * 0.92f // 92% fixed height

    var dragOffset by remember { mutableStateOf(0f) }

    // Smooth spring animation for sheet entrance/exit
    val targetOffset = if (visible) 0f else sheetMaxHeight
    val animatedOffset by animateFloatAsState(
        targetValue = targetOffset,
        animationSpec = spring(
            dampingRatio = 0.75f,
            stiffness = 300f
        ),
        label = "sheetOffset"
    )

    // Backdrop alpha animation
    val backdropAlpha by animateFloatAsState(
        targetValue = if (visible) 0.5f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "backdropAlpha"
    )

    // Get keyboard height for moving sheet up (not shrinking)
    val imeInsets = WindowInsets.ime
    val imeBottomPx = imeInsets.getBottom(density)

    LaunchedEffect(visible) {
        if (!visible) {
            dragOffset = 0f
        }
    }

    if (visible || animatedOffset < sheetMaxHeight) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Backdrop with smooth fade
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backdropAlpha))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, _ -> }
                    }
            )

            // Bottom sheet with keyboard-aware positioning
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { sheetMaxHeight.toDp() })
                    .align(Alignment.BottomCenter)
                    .offset {
                        // Move sheet up with keyboard, add drag offset, add animation offset
                        IntOffset(
                            x = 0,
                            y = (animatedOffset + dragOffset - imeBottomPx.toFloat()).roundToInt()
                        )
                    }
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        spotColor = Color.Black.copy(alpha = 0.12f),
                        clip = false
                    )
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                // Dismiss if dragged down more than 40%
                                if (dragOffset > sheetMaxHeight * 0.4f) {
                                    onDismiss()
                                }
                                dragOffset = 0f
                            },
                            onDragCancel = {
                                dragOffset = 0f
                            },
                            onVerticalDrag = { _, dragAmount ->
                                // Only allow dragging down
                                val newOffset = (dragOffset + dragAmount).coerceAtLeast(0f)
                                dragOffset = newOffset
                            }
                        )
                    }
            ) {
                // Layer 1: Linear gradient (180deg, #000926 50%, #001F52 100%)
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

                // Layer 2: Radial gradient glow
                // radial-gradient(245.4% 100% at 50% 100%, rgba(0,126,235,0.12) 0%, rgba(0,126,235,0.072) 20%, rgba(0,126,235,0) 68%)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x1F007EEB), // rgba(0,126,235,0.12)
                                    Color(0x12007EEB), // rgba(0,126,235,0.072)
                                    Color(0x00007EEB), // rgba(0,126,235,0) at 68%
                                    Color(0x00007EEB)
                                ),
                                center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                                radius = 1400f
                            )
                        )
                )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Pull handle - 36dp × 4dp, 12dp radius, white 30%
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                            .align(Alignment.CenterHorizontally)
                    )

                    // Content area with scroll
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

