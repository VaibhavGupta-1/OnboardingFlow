package com.example.onboardingflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onboardingflow.data.UserPreferencesRepository
import com.example.onboardingflow.onboarding.OnboardingScreen
import com.example.onboardingflow.onboarding.OnboardingViewModel
import com.example.onboardingflow.onboarding.OnboardingViewModelFactory
import com.example.onboardingflow.onboarding.MainOnboardingScreen
import com.example.onboardingflow.otp.OtpScreen
import com.example.onboardingflow.success.SuccessScreen

sealed class ShieldDestination(val route: String) {
    data object Onboarding : ShieldDestination("onboarding")
    data object MainOnboarding : ShieldDestination("main_onboarding")
    data object Otp : ShieldDestination("otp")
    data object Success : ShieldDestination("success")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val repository = remember { UserPreferencesRepository(context) }
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(repository)
    )
    val hasCompletedOnboarding by onboardingViewModel.hasCompletedOnboarding.collectAsState()

    val startDestination = if (hasCompletedOnboarding) {
        ShieldDestination.Otp.route
    } else {
        ShieldDestination.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ShieldDestination.Onboarding.route) {
            OnboardingScreen(
                viewModel = null,
                onCompleted = {
                    navController.navigate(ShieldDestination.MainOnboarding.route) {
                        popUpTo(ShieldDestination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(ShieldDestination.MainOnboarding.route) {
            MainOnboardingScreen(
                onContinue = {
                    onboardingViewModel.setOnboardingCompleted()
                    navController.navigate(ShieldDestination.Otp.route) {
                        popUpTo(ShieldDestination.MainOnboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(ShieldDestination.Otp.route) {
            OtpScreen(
                onVerified = {
                    navController.navigate(ShieldDestination.Success.route) {
                        popUpTo(ShieldDestination.Otp.route) { inclusive = false }
                    }
                },
                onBack = {
                    if (!hasCompletedOnboarding) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(ShieldDestination.Success.route) {
            SuccessScreen()
        }
    }
}
