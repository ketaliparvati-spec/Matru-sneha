package com.example.matrusneha.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrusneha.data.local.MotherProfile
import com.example.matrusneha.ui.theme.*

@Composable
fun ProfileScreen(profile: MotherProfile?, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mother's Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            tint = SecondaryColor,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (profile != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ProfileRow("Name", profile.name)
                    ProfileRow("Age", "${profile.age} years")
                    ProfileRow("Village / City", profile.village)
                    ProfileRow("Phone Number", profile.phoneNumber)
                    ProfileRow("Blood Group", profile.bloodGroup.ifEmpty { "Not Provided" })
                    
                    val currentWeek = profile.pregnancyMonth * 4
                    val estimatedRemainingWeeks = 40 - currentWeek
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Estimated Delivery: ~${estimatedRemainingWeeks} weeks away",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
            ) {
                Text("Logout / Clear Data", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text("No profile found.", color = TextSecondary)
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontWeight = FontWeight.Medium)
        Text(text = value, color = TextPrimary, fontWeight = FontWeight.Bold)
    }
}
