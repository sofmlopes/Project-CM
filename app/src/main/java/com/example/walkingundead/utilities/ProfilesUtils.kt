package com.example.walkingundead.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkingundead.models.Profile

/**
 * Displays the user's email and skills.
 */
@Composable
fun ProfileItem(profile: Profile) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp) // 5dp vertical space between cards
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name and Skills
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 20.dp)
        ) {
            // Display the email
            Text(
                text = profile.email ?: "",
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(8.dp)) // Spacing between email and skills

            // Display skills
            if (profile.skills.isEmpty()) {
                Text(
                    text = "No skills available",
                    style = TextStyle(color = Color.Gray)
                )
            } else {
                profile.skills.forEach { skill ->
                    Text(
                        text = "- ${skill.name}",
                        style = TextStyle(color = Color.DarkGray, fontSize = 14.sp)
                    )
                }
            }
        }
    }
}
