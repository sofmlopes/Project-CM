package com.example.walkingundead.utilities

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkingundead.screens.SkillsPickerScreen
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme

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