package com.example.matrusneha.ui.screens.danger

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.data.local.MotherProfile
import com.example.matrusneha.ui.theme.*

@Composable
fun EmergencyContactsScreen(
    profile: MotherProfile?,
    onSave: (String, String, String) -> Unit
) {
    val context = LocalContext.current
    
    var doctorNumber by remember(profile) { mutableStateOf(profile?.doctorNumber ?: "108") }
    var ashaWorkerNumber by remember(profile) { mutableStateOf(profile?.ashaWorkerNumber ?: "") }
    var familyNumber by remember(profile) { mutableStateOf(profile?.familyNumber ?: "") }

    var showSavedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emergency Contacts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Save numbers to call quickly during an emergency.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ContactInputCard("Doctor / Ambulance", doctorNumber, { doctorNumber = it }, context)
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInputCard("ASHA Worker", ashaWorkerNumber, { ashaWorkerNumber = it }, context)
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInputCard("Family Member", familyNumber, { familyNumber = it }, context)
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onSave(doctorNumber, ashaWorkerNumber, familyNumber)
                showSavedMessage = true
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Save Contacts", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (showSavedMessage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("✅ Contacts saved securely", color = SafeColor, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInputCard(
    title: String,
    number: String,
    onNumberChange: (String) -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = number,
                    onValueChange = onNumberChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter number") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Button(
                    onClick = {
                        if (number.isNotEmpty()) {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$number")
                            }
                            context.startActivity(dialIntent)
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                }
            }
        }
    }
}
