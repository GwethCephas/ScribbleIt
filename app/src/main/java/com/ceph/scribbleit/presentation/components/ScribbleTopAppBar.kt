package com.ceph.scribbleit.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScribbleTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSortByTitleClick: () -> Unit,
    onSortByTimestampClick: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "ScribbleIt",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary

        ),
        actions = {
            IconButton(
                onClick = onToggleTheme
            ) {

                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            SortMenu(
                onSortByTitleClick = onSortByTitleClick,
                onSortByTimestampClick = onSortByTimestampClick,
                onDeleteClick = onDeleteClick
            )


        }

    )
}