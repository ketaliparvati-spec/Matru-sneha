package com.example.matrusneha.ui.screens.growth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.ui.theme.*

data class GrowthInfo(
    val emoji: String,
    val sizeText: String,
    val developmentTip1: String,
    val developmentTip2: String
)

fun getGrowthDataForWeek(week: Int): GrowthInfo {
    return when {
        week <= 4 -> GrowthInfo("🌱", "Poppy Seed", "The neural tube is forming", "Start taking folic acid")
        week in 5..8 -> GrowthInfo("🫐", "Blueberry", "Heartbeat can be detected", "Rest often to avoid fatigue")
        week in 9..12 -> GrowthInfo("🍋", "Lemon", "Fingers and toes forming", "Drink plenty of water")
        week in 13..16 -> GrowthInfo("🥑", "Avocado", "Baby can make facial expressions", "Eat calcium-rich foods")
        week in 17..20 -> GrowthInfo("🥭", "Mango", "You might feel first kicks!", "Increase your iron intake")
        week in 21..24 -> GrowthInfo("🥥", "Coconut", "Hearing is developing", "Talk and sing to your baby")
        week in 25..28 -> GrowthInfo("🍆", "Eggplant", "Lungs are maturing", "Monitor kick counts daily")
        week in 29..32 -> GrowthInfo("🥬", "Cabbage", "Baby is gaining weight fast", "Prepare your hospital bag")
        week in 33..36 -> GrowthInfo("🍈", "Melon", "Bones are hardening", "Rest and sleep on your side")
        else -> GrowthInfo("🍉", "Watermelon", "Baby is fully developed", "Get ready for delivery!")
    }
}

@Composable
fun BabyGrowthScreen(currentWeek: Int) {
    val growthData = getGrowthDataForWeek(currentWeek)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Week $currentWeek",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Illustration Area
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(SecondaryColor, shape = RoundedCornerShape(75.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(growthData.emoji, fontSize = 80.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your baby is the size of a ${growthData.sizeText}!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Development Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("❤️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(growthData.developmentTip1, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🍎", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(growthData.developmentTip2, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                }
            }
        }
    }
}
