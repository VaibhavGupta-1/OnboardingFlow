package com.example.onboardingflow.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ShieldLightColors = lightColorScheme(
    primary = ShieldBlue,
    onPrimary = ShieldOnPrimary,
    primaryContainer = ShieldBlueMid,
    onPrimaryContainer = ShieldOnPrimary,
    secondary = ShieldCyan,
    onSecondary = ShieldOnPrimary,
    background = ShieldBackground,
    onBackground = ShieldOnSurface,
    surface = ShieldSurface,
    onSurface = ShieldOnSurface,
    surfaceContainerHighest = ShieldBlueDark,
    error = ShieldError,
    onError = ShieldOnPrimary,
    errorContainer = ShieldErrorContainer,
    outline = ShieldOutline
)

private val ShieldDarkColors = darkColorScheme(
    primary = ShieldBlue,
    onPrimary = ShieldOnPrimary,
    primaryContainer = ShieldBlueDark,
    onPrimaryContainer = ShieldOnPrimary,
    secondary = ShieldCyan,
    onSecondary = ShieldOnPrimary,
    background = Color(0xFF050B13),
    onBackground = ShieldSurface,
    surface = Color(0xFF0A1320),
    onSurface = ShieldSurface,
    surfaceContainerHighest = ShieldBlueMid,
    error = ShieldError,
    onError = ShieldOnPrimary,
    errorContainer = ShieldErrorContainer,
    outline = ShieldOutline
)

@Composable
fun OnboardingFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ShieldDarkColors else ShieldLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShieldTypography,
        content = content
    )
}