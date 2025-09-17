package com.ufg.estudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.graphics.Brush
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

@Composable
fun SplashScreenWithAnimation() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        AnimatedSplashScreen {
            showSplash = false
        }
    } else {
        EstudiantesScreen()
    }
}

@Composable
fun AnimatedSplashScreen(onSplashEnd: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Animación de onda expansiva y fade in
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 1500
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500
            )
        )

        // Pausa en la pantalla
        delay(1500)
        showButton = true
    }

    // Animación de degradado de fondo
    val backgroundBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF5A6FE7), Color(0xFF5ED5B7)),
            start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Icono de la app - Icono temporal de una escuela
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_agenda),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Título de la app
            Text(
                text = "Gestión de Estudiantes",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            // Subtítulo
            Text(
                text = "Sistema integral de administración académica",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(animationSpec = tween(1000, easing = LinearEasing)) + slideInVertically(initialOffsetY = { it })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(onClick = onSplashEnd) {
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

@Preview(showBackground = true)
@Composable
fun AnimatedSplashPreview() {
    MaterialTheme {
        Surface {
            AnimatedSplashScreen {}
        }
    }
}

// Composable de ejemplo para la pantalla principal
@Composable
fun EstudiantesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Contenido de la pantalla principal
    }
}
