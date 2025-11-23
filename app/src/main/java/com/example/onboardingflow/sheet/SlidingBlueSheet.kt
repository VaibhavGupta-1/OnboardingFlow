package com.example.onboardingflow.sheet

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.absoluteValue

@Composable
fun SlidingBlueSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val sheetHeightPx = screenHeightPx * 0.92f // 92% of screen height

    var offsetY by remember { mutableStateOf(sheetHeightPx) }
    var dragOffset by remember { mutableStateOf(0f) }

    val animatedOffset by animateFloatAsState(
        targetValue = if (visible) 0f else sheetHeightPx,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sheetSlide"
    )

    LaunchedEffect(visible) {
        if (visible) {
            offsetY = 0f
            dragOffset = 0f
        } else {
            offsetY = sheetHeightPx
        }
    }

    if (visible || animatedOffset < sheetHeightPx) {
        Popup(
            alignment = Alignment.BottomCenter,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            ),
            onDismissRequest = onDismiss
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
            ) {
                // Backdrop overlay with blur effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, _ -> }
                        }
                )

                // Bottom sheet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(density) { sheetHeightPx.toDp() })
                        .align(Alignment.BottomCenter)
                        .offset(y = with(density) { (animatedOffset + dragOffset).toDp() })
                        .shadow(
                            elevation = 24.dp,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                            spotColor = Color.Black.copy(alpha = 0.12f)
                        )
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    // Dismiss if dragged down more than 40%
                                    if (dragOffset > sheetHeightPx * 0.4f) {
                                        onDismiss()
                                    }
                                    dragOffset = 0f
                                },
                                onDragCancel = {
                                    dragOffset = 0f
                                },
                                onVerticalDrag = { _, dragAmount ->
                                    val newOffset = (dragOffset + dragAmount).coerceAtLeast(0f)
                                    dragOffset = newOffset
                                }
                            )
                        }
                ) {
                    // Layer 1: Linear gradient background
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF000926), // 50%
                                        Color(0xFF001F52)  // 100%
                                    )
                                )
                            )
                    )

                    // Layer 2: Radial gradient glow at bottom
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0x1F007EEB), // rgba(0,126,235,0.12) at 0%
                                        Color(0x12007EEB), // rgba(0,126,235,0.072) at 20%
                                        Color(0x00007EEB), // rgba(0,126,235,0) at 68%
                                        Color(0x00007EEB)  // rgba(0,126,235,0) at 100%
                                    ),
                                    center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                                    radius = 1200f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Pull handle
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                                .align(Alignment.CenterHorizontally)
                        )

                        // Sheet content
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp)
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

