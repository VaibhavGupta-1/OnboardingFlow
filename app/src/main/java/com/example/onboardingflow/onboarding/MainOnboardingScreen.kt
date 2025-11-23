package com.example.onboardingflow.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onboardingflow.R

@Composable
fun MainOnboardingScreen(
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF81D3FE), // Top: light blue
                        Color(0xFFFFFFFF)  // Bottom: white
                    )
                )
            )
    ) {
        // Soft radial blue glow behind top icon
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x4081D3FE),
                            Color(0x0081D3FE)
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.15f),
                        radius = 600f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(132.dp))

            // Shield Icon (glossy security/shield lock icon)
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 110.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shield_icon),
                    contentDescription = "Shield Icon",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Heading
            Text(
                text = "Private by Design.\nYou're in Control.",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF011E4F),
                textAlign = TextAlign.Center,
                lineHeight = 38.4.sp, // 120% of 32sp
                letterSpacing = (-0.64).sp, // -2% of 32sp
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Shield protects you from scams, risky links, and harmful apps, all while keeping your data private and secure.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF011E4F).copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 22.4.sp, // 140% of 14sp
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Feature List (no border/stroke)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FeatureRow(
                    icon = "🔒",
                    text = "All checks happen on your phone; contacts and chats stay private."
                )
                FeatureRow(
                    icon = "✓",
                    text = "We request only permissions needed to keep you safe."
                )
                FeatureRow(
                    icon = "🕒",
                    text = "Control your data access anytime. We never sell your information."
                )
            }

            Spacer(modifier = Modifier.height(80.dp))

            Spacer(modifier = Modifier.weight(1f))

            // CTA Button (pill-shaped with gradient)
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .width(312.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp), // Fully rounded pill shape
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2094ED), // Top
                                    Color(0xFF0067FF)  // Bottom
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "I understand",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FeatureRow(icon: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Rounded blue circle with white symbol
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2094ED),
                            Color(0xFF0067FF)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        // Feature text
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF011E4F).copy(alpha = 0.8f),
            lineHeight = 19.6.sp, // 140% of 14sp
            modifier = Modifier.weight(1f)
        )
    }
}

