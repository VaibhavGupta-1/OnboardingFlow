# Shield Onboarding & OTP Verification Flow

## Overview
A complete Android onboarding and OTP verification flow built with **Kotlin** and **Jetpack Compose**, featuring pixel-perfect designs, smooth animations, and full state persistence.

## Features Implemented

### ✅ Onboarding Carousel
- **Horizontal Pager** with swipe gesture support
- **3 onboarding screens** with custom Compose illustrations
- **Dot indicators** showing current page
- **Smooth transitions** between pages
- **Adaptive CTA button** ("Next" / "Get Started")
- **State persistence** via DataStore - skip onboarding on subsequent app launches

### ✅ OTP Verification Flow

#### Screen 1: Phone Number Entry
- Country code selector (fixed to +91)
- Numeric-only input with 10-digit validation
- Real-time error handling
- "Continue" button with enabled/disabled states
- Haptic feedback on button press

#### Screen 2: OTP Entry
- **6-digit OTP input** with individual cells
- **Auto-focus and auto-advance** between cells
- **60-second countdown timer** with resend functionality
- **Error state handling** (invalid OTP, empty fields)
- **Haptic feedback** on digit entry
- Visual feedback (focus states, error states)

#### Screen 3: Success Screen
- Animated success checkmark
- Confirmation message
- Gradient background matching design system
- "Continue" CTA to complete flow

### ✅ Technical Implementation

**Architecture:**
- MVVM pattern with ViewModels
- State management via StateFlow
- Repository pattern for data persistence
- ViewModelFactory for dependency injection

**Persistence:**
- DataStore Preferences for onboarding completion
- Phone number storage
- Automatic skip logic for returning users

**UI/UX:**
- Pure Compose implementation (no XML)
- Custom illustrations using Canvas and shapes
- Responsive layouts for all screen sizes
- Material 3 design system
- Custom color palette and typography
- Smooth page transitions
- Haptic feedback integration

## Project Structure

```
app/src/main/java/com/example/onboardingflow/
├── data/
│   └── UserPreferencesRepository.kt     # DataStore persistence
├── navigation/
│   └── AppNavGraph.kt                   # Navigation graph
├── onboarding/
│   ├── OnboardingScreen.kt              # Main carousel screen
│   ├── OnboardingIllustrations.kt       # Custom Compose illustrations
│   ├── OnboardingViewModel.kt           # State management
│   ├── OnboardingViewModelFactory.kt    # ViewModel factory
│   └── model/
│       └── OnboardingPage.kt            # Data model
├── otp/
│   ├── OtpScreen.kt                     # OTP flow coordinator
│   ├── OtpFlowScreens.kt                # Phone & OTP entry screens
│   ├── OtpViewModel.kt                  # OTP state management
│   ├── OtpViewModelFactory.kt           # ViewModel factory
│   └── OtpUiState.kt                    # UI state model
├── success/
│   └── SuccessScreen.kt                 # Verification success screen
��── ui/theme/
│   ├── Color.kt                         # Custom color palette
│   ├── Type.kt                          # Typography system
│   └── Theme.kt                         # App theme
└── MainActivity.kt                      # App entry point
```

## Design System

### Colors
- **Primary Blue**: `#0073FF`
- **Dark Blue**: `#00204B`
- **Cyan**: `#4DD7FF`
- **Success Green**: `#2FDAA7`
- **Error Red**: `#FF5C64`

### Typography
- **Display**: 44sp, Bold
- **Headline**: 28sp, Bold
- **Title**: 18sp, Medium
- **Body**: 16sp, Regular

## How to Build

```bash
# Build debug APK
./gradlew :app:assembleDebug

# APK location
app/build/outputs/apk/debug/app-debug.apk
```

## How to Test

### Onboarding Flow
1. Launch app (first time)
2. Swipe through 3 onboarding screens
3. Tap "Get Started" on final screen
4. Onboarding completion is saved

### OTP Flow
1. Enter 10-digit phone number
2. Tap "Continue"
3. Enter any 6-digit OTP
4. OTP auto-verifies after 6 digits
5. Success screen appears

### Persistence Testing
1. Complete onboarding once
2. Close and restart app
3. App should skip onboarding

## Key Dependencies

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.3")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.1.1")
```

## Mock Data Details

### OTP Verification
- **Mock OTP**: Any 6-digit number works (123456, 000000, etc.)
- **Verification Delay**: 1.5 seconds to simulate API call
- **Timer**: 60-second countdown before resend enabled

### Persistence
- Onboarding completion stored in DataStore
- Phone number saved after OTP send
- No actual SMS integration (fully mocked)

## Future Enhancements

- [ ] Country code selector dropdown
- [ ] Real SMS OTP integration
- [ ] Biometric authentication option
- [ ] Dark mode support
- [ ] Accessibility improvements
- [ ] Unit tests for ViewModels
- [ ] UI tests for flows

## Notes

- **All screens are 100% Compose** - No XML layouts
- **Illustrations are code-based** - Using Canvas, shapes, and paths
- **Fully responsive** - Adapts to different screen sizes
- **Production-ready structure** - Clean architecture, separation of concerns

---

**Built with ❤️ using Kotlin & Jetpack Compose**

