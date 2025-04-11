package com.ceph.scribbleit.presentation.scribbles

import com.ceph.scribbleit.data.local.ScribbleEntity

data class ScribbleUiState(
    val scribbles: List<ScribbleEntity> = emptyList(),
    var isDialogVisible: Boolean = false,
    val currentTitle: String = "",
    val currentContent: String = "",
    val sortBy: SortType = SortType.TIMESTAMP
)

enum class SortType {
    TITLE, TIMESTAMP
}