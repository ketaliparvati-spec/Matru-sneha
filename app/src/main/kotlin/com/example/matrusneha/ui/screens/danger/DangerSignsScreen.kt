package com.example.matrusneha.ui.screens.danger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.ui.theme.*

@Composable
fun DangerSignsScreen(
    onNavigateToContacts: () -> Unit
) {
    val scrollState = rememberScrollState()
    var hasDangerSign by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Emergency Danger Signs",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = ErrorColor,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "Select any symptoms you are experiencing right now:",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        val symptoms = listOf(
            "High swelling in hands/face",
            "Bleeding",
            "Severe headache or blurred vision",
            "High fever",
            "Reduced baby movement"
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                symptoms.forEach { symptom ->
                    var checked by remember { mutableStateOf(false) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { 
                                checked = it 
                                hasDangerSign = hasDangerSign || it // Simplified logic for demo
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ErrorColor,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(text = symptom, color = TextPrimary)
                    }
                }
            }
        }

        if (hasDangerSign) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ErrorColor.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(2.dp, ErrorColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Visit nearest PHC immediately.",
                        color = ErrorColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Call Emergency */ },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Call Emergency Contact", color = Color.White)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToContacts,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Icon(Icons.Default.Call, contentDescription = "Emergency Contacts", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "View Emergency Contacts",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
