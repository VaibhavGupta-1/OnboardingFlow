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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SlidingBlueSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        val scope = rememberCoroutineScope()

        // Calculate 92% of screen height as FIXED value
        val sheetHeightDp = maxHeight * 0.92f
        val sheetHeightPx = with(density) { sheetHeightDp.toPx() }

        // Animatable for smooth translation (starts off-screen)
        val translationY = remember {
            Animatable(initialValue = sheetHeightPx)
        }

        // Animatable for backdrop alpha
        val backdropAlpha = remember { Animatable(0f) }

        // Drag offset state
        var dragOffset by remember { mutableStateOf(0f) }

        // Keyboard insets - used only for slight translation, NOT for resizing
        val imeInsets = WindowInsets.ime
        val imeBottomPx = imeInsets.getBottom(density).toFloat()

        // Animate sheet visibility with proper lifecycle
        LaunchedEffect(visible, sheetHeightPx) {
            if (sheetHeightPx > 0f) {
                if (visible) {
                    // Slide up + fade in
                    launch {
                        translationY.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = 0.75f,
                                stiffness = 300f
                            )
                        )
                    }
                    launch {
                        backdropAlpha.animateTo(
                            targetValue = 0.5f,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                } else {
                    // Slide down + fade out
                    launch {
                        translationY.animateTo(
                            targetValue = sheetHeightPx,
                            animationSpec = spring(
                                dampingRatio = 0.75f,
                                stiffness = 300f
                            )
                        )
                    }
                    launch {
                        backdropAlpha.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                }
            }
        }

        // Only render if visible or animating
        if (visible || translationY.value < sheetHeightPx) {
            // Backdrop with smooth fade
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backdropAlpha.value))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, _ -> }
                    }
            )

            // Bottom sheet - FIXED height (92%), positioned at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeightDp)  // FIXED height - no fillMaxSize, no unbounded
                    .align(Alignment.BottomCenter)
                    .offset {
                        // Translation: animation + drag - keyboard (moves up, doesn't resize)
                        IntOffset(
                            x = 0,
                            y = (translationY.value + dragOffset - imeBottomPx).roundToInt()
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
                                scope.launch {
                                    // Dismiss if dragged down ≥ 40%
                                    if (dragOffset > sheetHeightPx * 0.4f) {
                                        translationY.animateTo(
                                            targetValue = sheetHeightPx,
                                            animationSpec = spring(dampingRatio = 0.75f, stiffness = 300f)
                                        )
                                        backdropAlpha.animateTo(
                                            targetValue = 0f,
                                            animationSpec = tween(durationMillis = 300)
                                        )
                                        onDismiss()
                                    }
                                    dragOffset = 0f
                                }
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
                // Layer 1: Linear gradient (#000926 → #001F52)
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

                // Layer 2: Radial gradient glow (Figma exact)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x1F007EEB), // rgba(0,126,235,0.12)
                                    Color(0x12007EEB), // rgba(0,126,235,0.072)
                                    Color(0x00007EEB), // rgba(0,126,235,0)
                                    Color(0x00007EEB)
                                ),
                                center = androidx.compose.ui.geometry.Offset(0.5f, 1.0f),
                                radius = 1400f
                            )
                        )
                )

                // Column with bounded height
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // BOUNDED scrollable content area using weight(1f)
                    // This prevents infinite height constraints
                    Box(
                        modifier = Modifier
                            .weight(1f)  // Takes remaining space - BOUNDED height
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())  // Safe: container is bounded
                            .padding(horizontal = 24.dp)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

