package com.example.matrusneha.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.matrusneha.data.local.NutritionLog
import com.example.matrusneha.ui.theme.*

@Composable
fun NutritionScreen(
    nutritionLogs: List<NutritionLog>,
    onToggleLog: (NutritionLog, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    // Group logs by category
    val energyFoods = nutritionLogs.filter { it.category == "Energy Foods" }
    val proteinFoods = nutritionLogs.filter { it.category == "Protein Foods" }
    val ironFoods = nutritionLogs.filter { it.category == "Iron Foods" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Nutrition Checklist",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Energy Foods
        if (energyFoods.isNotEmpty()) {
            NutritionCategoryCard("Energy Foods", energyFoods, onToggleLog)
        }

        // Protein Foods
        if (proteinFoods.isNotEmpty()) {
            NutritionCategoryCard("Protein Foods", proteinFoods, onToggleLog)
        }

        // Iron Foods
        if (ironFoods.isNotEmpty()) {
            NutritionCategoryCard("Iron Foods", ironFoods, onToggleLog)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Basic AI Warning Logic (Phase 1)
        val ironCheckedCount = ironFoods.count { it.isCompleted }
        if (ironCheckedCount == 0 && ironFoods.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarningColor.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarningColor)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡 Please eat iron-rich foods today for baby's healthy growth.",
                        color = WarningColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionCategoryCard(
    title: String, 
    logs: List<NutritionLog>, 
    onToggleLog: (NutritionLog, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            logs.forEach { log ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = log.isCompleted,
                        onCheckedChange = { isChecked -> onToggleLog(log, isChecked) },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryColor)
                    )
                    Text(text = log.foodName, color = TextSecondary)
                }
            }
        }
    }
}
