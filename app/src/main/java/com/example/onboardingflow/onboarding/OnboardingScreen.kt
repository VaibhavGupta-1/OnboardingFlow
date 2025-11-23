package com.example.onboardingflow.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import com.example.onboardingflow.R
import com.example.onboardingflow.onboarding.model.OnboardingPage
import com.example.onboardingflow.ui.theme.ShieldSurface
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onCompleted: () -> Unit,
    pages: List<OnboardingPage> = defaultPages(),
    viewModel: OnboardingViewModel? = null
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { pageIndex ->
                OnboardingCard(page = pages[pageIndex])
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(innerPadding)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title text overlay
                val currentPage = pages[pagerState.currentPage]
                if (currentPage.title.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .width(312.dp),
//                            .padding(bottom = 35.dp)
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentPage.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            letterSpacing = (-0.48).sp,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                AnimatedDotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = pagerState.currentPage
                )

                Spacer(modifier = Modifier.height(70.dp))

                val isLast = pagerState.currentPage == pages.lastIndex
                PrimaryButton(
                    text = if (isLast) "Get Started" else "Next",
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                ) {
                    if (isLast) {
                        viewModel?.setOnboardingCompleted()
                        onCompleted()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingCard(page: OnboardingPage) {
    Image(
        painter = painterResource(id = page.imageRes),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun AnimatedDotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
        repeat(totalDots) { index ->

            val isSelected = index == selectedIndex

            // Animate width (pill ↔ dot)
            val animatedWidth by animateDpAsState(
                targetValue = if (isSelected) 20.dp else 4.dp,
                animationSpec = TweenSpec(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "dotWidth"
            )

            // Animate color fade
            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                animationSpec = TweenSpec(durationMillis = 300),
                label = "dotColor"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(4.dp)
                    .clip(if (isSelected) RoundedCornerShape(4.dp) else CircleShape)
                    .background(animatedColor)
            )
        }
    }
}


@Composable
private fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(56.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF0066FF)
        ),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

private fun defaultPages(): List<OnboardingPage> = listOf(
    OnboardingPage(
        imageRes = R.drawable.onboarding_1,
        title = "Get instant alerts for scam calls, suspicious texts, harmful apps, and breaches"
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_2,
        title = "Protect your family from scams with real-time alerts and safety monitoring"
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_3,
        title = "Recover lost money if tricked, with Shield Protect up to Rs 1,00,000"
    )
)

