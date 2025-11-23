package com.example.onboardingflow.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF000926), Color(0xFF001F52))
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Checkmark Circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF167CE3),
                            Color(0xFF41A9F7)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                fontSize = 64.sp,
                color = Color.White,
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Your phone is\nverified securely.",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                lineHeight = 32.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You can now proceed with your account setup",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp
            ),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
