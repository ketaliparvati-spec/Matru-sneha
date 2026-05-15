package com.example.matrusneha.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.R
import com.example.matrusneha.data.local.MotherProfile
import com.example.matrusneha.data.local.NutritionLog
import com.example.matrusneha.ui.theme.*
import com.example.matrusneha.ui.util.LocaleHelper
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import java.util.Locale

@Composable
fun HomeScreen(
    profile: MotherProfile?,
    kickCount: Int,
    nutritionLogs: List<NutritionLog>,
    aiSuggestion: String?,
    onNavigateToKickCounter: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToDangerSigns: () -> Unit,
    onNavigateToGrowth: () -> Unit,
    onNavigateToCalendar: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Calculate Week roughly (month * 4)
    val currentWeek = profile?.pregnancyMonth?.times(4) ?: 1
    
    // Calculate Nutrition Score
    val completedNutrition = nutritionLogs.count { it.isCompleted }
    val totalNutrition = nutritionLogs.size.takeIf { it > 0 } ?: 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Quick Language Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Default.PregnantWoman,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val displayName = profile?.name ?: "Mother"
                Text(
                    text = stringResource(id = R.string.welcome_message, displayName),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            }
            
            Row {
                TextButton(
                    onClick = { 
                        LocaleHelper.setLocale(context, "en")
                        (context as? Activity)?.recreate()
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("EN", fontWeight = FontWeight.Bold, color = if (Locale.getDefault().language == "en") PrimaryColor else TextSecondary)
                }
                Text(
                    "|", 
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = TextSecondary.copy(alpha = 0.5f)
                )
                TextButton(
                    onClick = { 
                        LocaleHelper.setLocale(context, "kn")
                        (context as? Activity)?.recreate()
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("KN", fontWeight = FontWeight.Bold, color = if (Locale.getDefault().language == "kn") PrimaryColor else TextSecondary)
                }
            }
        }

        // Information Grid (2x2)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(title = stringResource(R.string.pregnancy_week), value = "Week $currentWeek", color = TertiaryColor, modifier = Modifier.weight(1f))
            InfoCard(title = stringResource(R.string.nutrition_score), value = "$completedNutrition / $totalNutrition", color = SecondaryColor, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(title = stringResource(R.string.kick_count_today), value = "$kickCount kicks", color = PrimaryColor, modifier = Modifier.weight(1f))
            val healthStatus = if (kickCount >= 10 && completedNutrition >= 4) "Safe \uD83D\uDC9A" else "Check ⚠️"
            val statusColor = if (healthStatus.contains("Safe")) SafeColor else WarningColor
            InfoCard(title = stringResource(R.string.health_status), value = healthStatus, color = statusColor, modifier = Modifier.weight(1f))
        }

        // AI Suggestion Box
        if (aiSuggestion != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryColor.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryColor)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = aiSuggestion, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.quick_actions),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        // Big Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionButton(title = stringResource(R.string.action_kick_counter), color = PrimaryColor, onClick = onNavigateToKickCounter, modifier = Modifier.weight(1f))
            ActionButton(title = stringResource(R.string.action_nutrition), color = SafeColor, onClick = onNavigateToNutrition, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionButton(title = stringResource(R.string.action_baby_growth), color = TertiaryColor, onClick = onNavigateToGrowth, modifier = Modifier.weight(1f))
            ActionButton(title = stringResource(R.string.action_checkups), color = SecondaryColor, onClick = onNavigateToCalendar, modifier = Modifier.weight(1f))
        }

        // Emergency Warning Button
        Button(
            onClick = onNavigateToDangerSigns,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
        ) {
            Icon(Icons.Default.Warning, contentDescription = "Warning", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.danger_signs),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionButton(title: String, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (color == SecondaryColor) TextPrimary else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
