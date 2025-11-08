package com.billioapp.features.onboarding.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.onboarding_illustration
import billioapp.composeapp.generated.resources.financial_illustration
import billioapp.composeapp.generated.resources.circular_arrow_button
import billioapp.composeapp.generated.resources.onboard2
import billioapp.composeapp.generated.resources.onboard3
import billioapp.composeapp.generated.resources.onboard4
import com.billioapp.features.onboarding.presentation.OnboardingSlideData
import com.billioapp.core.theme.getBalooFontFamily
import kotlinx.coroutines.delay
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun OnboardingSlide(
    slide: OnboardingSlideData,
    onGetStartedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isFirstSlide = slide.title == "Billio"
    val isSecondSlide = slide.title == "Daha Akıllı Harca, Daha Çok Biriktir"
    val isThirdSlide = slide.title == "Faturalarını Unutma!"
    
    when {
        isFirstSlide -> {
            // İlk slide - mevcut animasyonlu tasarım
            FirstSlideContent(slide, onGetStartedClick, modifier)
        }
        isSecondSlide -> {
            // İkinci slide - yeni Figma tasarımı
            SecondSlideContent(slide, onGetStartedClick, modifier)
        }
        isThirdSlide -> {
            // Üçüncü slide - tutarlı tasarım
            ThirdSlideContent(slide, onGetStartedClick, modifier)
        }
        else -> {
            // Dördüncü slide - tutarlı tasarım
            FourthSlideContent(slide, onGetStartedClick, modifier)
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun FirstSlideContent(
    slide: OnboardingSlideData,
    onGetStartedClick: () -> Unit,
    modifier: Modifier
) {
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Animation values
    val logoScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )
    
    val logoAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutCubic
        ),
        label = "logoAlpha"
    )
    
    val imageScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "imageScale"
    )
    
    val imageAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 300,
            easing = EaseOutCubic
        ),
        label = "imageAlpha"
    )
    
    val buttonScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )
    
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 600,
            easing = EaseOutCubic
        ),
        label = "buttonAlpha"
    )
    
    // Start animation when composable is first composed
    LaunchedEffect(Unit) {
        delay(200) // Small delay for smooth start
        isVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBDE9DE))
            .padding(horizontal = 32.dp)
    ) {
        // Main content in center
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Üst kısım: Lottie solda, ikon sağda – modern, yan yana düzen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 32.dp)
                    .scale(imageScale)
                    .alpha(imageAlpha),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sol: Lottie animasyonu (para yağmuru)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0x33FFFFFF))
                        .padding(12.dp)
                ) {
                    val composition by rememberLottieComposition {
                        LottieCompositionSpec.JsonString(
                            Res.readBytes("drawable/kagitpara.json").decodeToString()
                        )
                    }
                    val lottiePainter = rememberLottiePainter(
                        composition = composition,
                        iterations = Compottie.IterateForever
                    )
                    Image(
                        painter = lottiePainter,
                        contentDescription = "Money rain",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Sağ: Domuzcuk ikon – küçültülmüş ve sağa hizalı
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0x33FFFFFF))
                        .padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.onboarding_illustration),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center)
                    )
                }
            }

            // BILLIO başlığı – iki animasyonun altında
            Text(
                text = slide.title,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getBalooFontFamily(),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )

            // Get Started butonu – başlığın altında
            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp)
                    .scale(buttonScale)
                    .alpha(buttonAlpha)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Alt orta: Cüzdan animasyonu – aynı kart stilinde, ortalanmış
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x33FFFFFF))
                    .padding(12.dp)
                    .height(140.dp)
            ) {
                val walletComposition by rememberLottieComposition {
                    LottieCompositionSpec.JsonString(
                        Res.readBytes("drawable/cuzdanli.json").decodeToString()
                    )
                }
                val walletPainter = rememberLottiePainter(
                    composition = walletComposition,
                    iterations = Compottie.IterateForever
                )
                Image(
                    painter = walletPainter,
                    contentDescription = "Wallet animation",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
private fun SecondSlideContent(
    slide: OnboardingSlideData,
    onGetStartedClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBDE9DE))
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp), // Reserve space for BILLIO text
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar at top
            Box(
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 40.dp)
                    .width(200.dp)
                    .height(4.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f) // 2/4 = 50% progress
                        .background(
                            Color(0xFF4CAF50), // Changed to green
                            RoundedCornerShape(2.dp)
                        )
                )
            }
            
            // PNG Financial Illustration at top
            Image(
                painter = painterResource(Res.drawable.financial_illustration),
                contentDescription = null,
                modifier = Modifier
                    .size(320.dp, 340.dp) // Increased from 280x300 to 320x340
                    .padding(bottom = 30.dp)
            )
            
            // Flexible spacer to center the text content
            Spacer(modifier = Modifier.weight(0.3f))
            
            // Title and Description grouped in center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("DAHA AKILLI HARCA ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF4CAF50))) {
                            append("DAHA ÇOK BİRİKTİR")
                        }
                    },
                    fontSize = 24.sp, // Reduced from 28.sp to 24.sp
                    fontWeight = FontWeight.Bold,
                    fontFamily = getBalooFontFamily(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = slide.description,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
            
            // Another flexible spacer
            Spacer(modifier = Modifier.weight(0.2f))
            
            // Modern Arrow Button in the flow
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onGetStartedClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color(0xFFBDE9DE),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Final spacer to push everything up
            Spacer(modifier = Modifier.weight(0.5f))
        }
        
        // BILLIO text at absolute bottom (outside main column flow)
        Text(
            text = "BILLIO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

@Composable
private fun ThirdSlideContent(
    slide: OnboardingSlideData,
    onGetStartedClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBDE9DE))
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp), // Reserve space for BILLIO text
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar at top
            Box(
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 40.dp)
                    .width(200.dp)
                    .height(4.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.75f) // 3/4 = 75% progress
                        .background(
                            Color(0xFF4CAF50), // Green color
                            RoundedCornerShape(2.dp)
                        )
                )
            }
            
            // PNG Onboarding3 Illustration at top
            Image(
                painter = painterResource(Res.drawable.onboard3),
                contentDescription = null,
                modifier = Modifier
                    .size(320.dp, 340.dp) // Same size as second slide
                    .padding(bottom = 30.dp)
            )
            
            // Flexible spacer to center the text content
            Spacer(modifier = Modifier.weight(0.3f))
            
            // Title and Description grouped in center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = slide.title,
                    fontSize = 24.sp, // Same as second slide
                    fontWeight = FontWeight.Bold,
                    fontFamily = getBalooFontFamily(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = slide.description,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
            
            // Another flexible spacer
            Spacer(modifier = Modifier.weight(0.2f))
            
            // Modern Arrow Button in the flow
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onGetStartedClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color(0xFFBDE9DE),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Final spacer to push everything up
            Spacer(modifier = Modifier.weight(0.5f))
        }
        
        // BILLIO text at absolute bottom (outside main column flow)
        Text(
            text = "BILLIO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

@Composable
private fun FourthSlideContent(
    slide: OnboardingSlideData,
    onGetStartedClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBDE9DE))
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp), // Reserve space for BILLIO text
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar at top
            Box(
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 40.dp)
                    .width(200.dp)
                    .height(4.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1.0f) // 4/4 = 100% progress
                        .background(
                            Color(0xFF4CAF50), // Green color
                            RoundedCornerShape(2.dp)
                        )
                )
            }
            
            // PNG Onboarding4 Illustration at top
            Image(
                painter = painterResource(Res.drawable.onboard4),
                contentDescription = null,
                modifier = Modifier
                    .size(320.dp, 340.dp) // Same size as second and third slides
                    .padding(bottom = 30.dp)
            )
            
            // Flexible spacer to center the text content
            Spacer(modifier = Modifier.weight(0.3f))
            
            // Title and Description grouped in center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = slide.title,
                    fontSize = 24.sp, // Same as second and third slides
                    fontWeight = FontWeight.Bold,
                    fontFamily = getBalooFontFamily(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = slide.description,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
            
            // Another flexible spacer
            Spacer(modifier = Modifier.weight(0.2f))
            
            // Modern Arrow Button in the flow
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onGetStartedClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color(0xFFBDE9DE),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Final spacer to push everything up
            Spacer(modifier = Modifier.weight(0.5f))
        }
        
        // BILLIO text at absolute bottom (outside main column flow)
        Text(
            text = "BILLIO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}