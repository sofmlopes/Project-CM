package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme

@Composable
fun SkillsPickerScreen() {
    // State for the list of skills and the new skill input
    var newSkill by remember { mutableStateOf("") }
    val skillsList = remember { mutableStateListOf("Cooking", "Crafting", "Fighting", "Navigation") }
    val selectedSkills = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Select Your Skills",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Display skills in a LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(skillsList) { skill ->
                SkillChip(
                    skill = skill,
                    isSelected = selectedSkills.contains(skill),
                    onSelect = { selected ->
                        if (selected) selectedSkills.add(skill) else selectedSkills.remove(skill)
                    }
                )
            }
        }

        // TextField for adding new skills
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = newSkill,
                onValueChange = { newSkill = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .height(50.dp),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (newSkill.isEmpty()) Text("Add a new skill", color = Color.Gray)
                        innerTextField()
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newSkill.isNotEmpty() && !skillsList.contains(newSkill)) {
                        skillsList.add(newSkill)
                        newSkill = ""
                    }
                })
            )
            Button(
                onClick = {
                    if (newSkill.isNotEmpty() && !skillsList.contains(newSkill)) {
                        skillsList.add(newSkill)
                        newSkill = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Save skills logic here
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Skills", color = Color.White)
        }
    }
}

@Composable
fun SkillChip(skill: String, isSelected: Boolean, onSelect: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
        contentColor = if (isSelected) Color.White else Color.Black,
        onClick = { onSelect(!isSelected) }
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSkillsPickerScreen() {
    WalkingUnDeadTheme {
        SkillsPickerScreen()
    }
}

