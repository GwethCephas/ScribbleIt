package com.ceph.scribbleit.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortMenu(
    onSortByTitleClick: () -> Unit,
    onSortByTimestampClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(end = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert, // Or any icon you want
            contentDescription = "Sort Menu",
            modifier = Modifier
                .clickable { expanded = true },
            tint = MaterialTheme.colorScheme.onPrimary
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sort by Title") },
                onClick = {
                    onSortByTitleClick()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort by Timestamp") },
                onClick = {
                    onSortByTimestampClick()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete All Scribbles",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    onDeleteClick()
                    expanded = false
                }
            )
        }
    }
}