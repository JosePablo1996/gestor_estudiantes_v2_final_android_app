package com.ufg.estudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ufg.estudiantes.ui.theme.EstudiantesScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SplashScreenWithAnimation()
                }
            }
        }
    }
}

/**
 * Manages the visibility of the splash screen.
 */
@Composable
fun SplashScreenWithAnimation() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        AnimatedSplashScreen(onSplashEnd = { showSplash = false })
    } else {
        EstudiantesScreen()
    }
}

/**
 * Creates an animated splash screen with a clean and modern design.
 *
 * This version uses a dark background with a bright accent color.
 * The animation is a "reveal" effect, where the logo and text appear
 * in a staggered fashion with a scale and fade-in animation.
 */
@Composable
fun AnimatedSplashScreen(onSplashEnd: () -> Unit) {
    // Animated states for the UI elements.
    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    var showButton by remember { mutableStateOf(false) }

    // The background color for a more elegant and modern design.
    val backgroundColor = Color(0xFF2C2F4E)
    val accentColor = Color(0xFF00C8FF)

    LaunchedEffect(Unit) {
        // Phase 1: Logo animation (scale and fade-in).
        logoScale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )
        logoAlpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )

        // Phase 2: Staggered fade-in for the text.
        delay(300)
        textAlpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 800)
        )

        // Phase 3: Delay and then show the button.
        delay(500)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App logo
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_agenda),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Title and subtitle with staggered animation.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha.value)
            ) {
                Text(
                    text = "Gestión de Estudiantes",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sistema integral de administración académica",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }

        // Animated visibility for the button.
        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(animationSpec = tween(1000, easing = LinearOutSlowInEasing)) +
                    slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = onSplashEnd,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Ingresar al Sistema")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Ingresar",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Preview for the animated splash screen.
 */
@Preview(showBackground = true)
@Composable
fun AnimatedSplashPreview() {
    MaterialTheme {
        Surface {
            AnimatedSplashScreen {}
        }
    }
}

/**
 * Placeholder for the main screen content.
 */
@Composable
fun EstudiantesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Main screen content goes here.
    }
}
