package com.ceph.scribbleit.presentation.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.presentation.components.EmptyScreen
import com.ceph.scribbleit.presentation.components.ScribbleBottomSheet
import com.ceph.scribbleit.presentation.components.ScribbleDialog
import com.ceph.scribbleit.presentation.components.ScribbleItem
import com.ceph.scribbleit.presentation.components.ScribbleTopAppBar
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiEvent
import com.ceph.scribbleit.presentation.scribbles.SortType
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean
) {

    val scribbleState by viewModel.scribbleState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedScribble by remember { mutableStateOf<ScribbleEntity?>(null) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ScribbleTopAppBar(
                scrollBehavior = scrollBehavior,
                onSortByTitleClick = {
                    scope.launch {
                        viewModel.onEvent(ScribbleUiEvent.SortScribbles(SortType.TITLE))
                    }
                },
                onSortByTimestampClick = {
                    scope.launch {
                        viewModel.onEvent(ScribbleUiEvent.SortScribbles(SortType.TIMESTAMP))
                    }
                },
                onToggleTheme = onToggleTheme,
                isDarkTheme = isDarkTheme,
                onDeleteClick = {
                    scope.launch {
                        viewModel.onEvent(ScribbleUiEvent.DeleteAllScribbles)
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(20.dp)
                    .clip(CircleShape),
                onClick = {
                    scope.launch {
                        viewModel.onEvent(ScribbleUiEvent.ShowDialog)
                        title = ""
                        content = ""
                    }

                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Scribble",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (scribbleState.scribbles.isEmpty()) {
                EmptyScreen()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(scribbleState.scribbles.size) { scribble ->
                        ScribbleItem(
                            scribbleEntity = scribbleState.scribbles[scribble],
                            onDeleteClick = {
                                scope.launch {
                                    viewModel.onEvent(
                                        ScribbleUiEvent.DeleteScribble(
                                            scribbleState.scribbles[scribble].id
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.clickable {
                                selectedScribble = scribbleState.scribbles[scribble]
                                isBottomSheetVisible = true
                            }
                        )

                    }
                }
            }
            if (isBottomSheetVisible && selectedScribble != null) {
                ScribbleBottomSheet(
                    title = selectedScribble!!.title,
                    content = selectedScribble!!.content,
                    timestamp = selectedScribble!!.timestamp,
                    onDismissRequest = {
                        isBottomSheetVisible = false
                        selectedScribble = null
                    }
                )
            }


            if (scribbleState.isDialogVisible) {
                ScribbleDialog(
                    titleValue = title,
                    contentValue = content,
                    onTitleChange = {
                        title = it

                    },
                    onContentChange = {
                        content = it
                    },
                    onDismissRequest = {
                        scope.launch {
                            viewModel.onEvent(ScribbleUiEvent.HideDialog)
                        }
                    },
                    onConfirmClick = {
                        scope.launch {
                            viewModel.onEvent(
                                ScribbleUiEvent.SaveScribble(
                                    title,
                                    content
                                )
                            )
                        }
                    },
                    onCancelClick = {
                        scope.launch {
                            viewModel.onEvent(ScribbleUiEvent.HideDialog)
                        }
                    }
                )
            }

        }
    }
}