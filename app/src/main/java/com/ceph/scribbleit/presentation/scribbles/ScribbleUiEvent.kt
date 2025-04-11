package com.ceph.scribbleit.presentation.scribbles

sealed class ScribbleUiEvent {

    data object LoadScribbles : ScribbleUiEvent()
    data class SaveScribble(val title: String, val content: String) : ScribbleUiEvent()
    data class DeleteScribble(val id: Int) : ScribbleUiEvent()
    data object DeleteAllScribbles : ScribbleUiEvent()
    data object ShowDialog : ScribbleUiEvent()
    data object HideDialog : ScribbleUiEvent()
    data class SortScribbles(val sortType: SortType) : ScribbleUiEvent()
    data class ShowSnackBar(val message: String) : ScribbleUiEvent()
}

