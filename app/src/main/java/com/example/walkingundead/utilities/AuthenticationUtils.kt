package com.example.walkingundead.utilities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.walkingundead.models.Contact
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.provider.RepositoryProvider.authRepository

@Composable
fun ContactItem(contact: Contact) {
    val database = RepositoryProvider.databaseRepository

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(contact.name ?: "Unknown", fontWeight = FontWeight.Bold)
            Text(contact.number ?: "No number")
        }
        IconButton(onClick = {
            database.removeContact(authRepository.getEmail(), contact)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Remove contact")
        }
    }
}