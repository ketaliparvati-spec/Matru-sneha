package com.example.matrusneha.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.matrusneha.ui.theme.PrimaryColor
import com.example.matrusneha.ui.theme.SecondaryColor

import com.example.matrusneha.ui.util.LocaleHelper

@Composable
fun SplashScreen(onLanguageSelected: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon / Logo Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, shape = RoundedCornerShape(60.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PregnantWoman,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(80.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Matru-Sneh",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Healthy Mother, Healthy Child",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f)
        )
        
        Text(
            text = "ಆರೋಗ್ಯವಂತ ತಾಯಿ, ಆರೋಗ್ಯವಂತ ಮಗು",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Language Selection Buttons
        Button(
            onClick = {
                LocaleHelper.setLocale(context, "en")
                onLanguageSelected()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("English", color = PrimaryColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                LocaleHelper.setLocale(context, "kn")
                onLanguageSelected()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
        ) {
            Text("ಕನ್ನಡ", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
