package com.example.matrusneha.ui.screens.kickcounter

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.ui.theme.*
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KickCounterScreen(
    todayKickCount: Int,
    history: List<Float>,
    onRecordKick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isPulsing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kick Counter",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Recording kicks helps track your baby's health. Aim for 10 kicks per day.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Large Counter Display
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(SecondaryColor.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = todayKickCount.toString(),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryColor
                )
                Text(
                    text = "KICKS TODAY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Record Button
        Button(
            onClick = {
                onRecordKick()
                scope.launch {
                    isPulsing = true
                    delay(300)
                    isPulsing = false
                }
            },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.Default.Add, 
                contentDescription = "Record Kick", 
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
        
        Text(
            text = "Tap to record a kick",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "7-Day Kick History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, bottom = 8.dp)
        )
        
        // Vico Chart with Real History
        val chartEntryModel = entryModelOf(*history.toTypedArray())
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Chart(
                    chart = columnChart(),
                    model = chartEntryModel,
                )
            }
        }
    }
}
