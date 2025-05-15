package com.ceph.scribbleit.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.domain.repository.ScribbleRepository
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiEvent
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiState
import com.ceph.scribbleit.presentation.scribbles.SortType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ScribbleRepository
) : ViewModel() {

    private val _scribbleState = MutableStateFlow(ScribbleUiState())
    val scribbleState = _scribbleState.asStateFlow()

    private val _scribbleEventFlow = MutableSharedFlow<ScribbleUiEvent>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val scribbleEventFlow = _scribbleEventFlow.asSharedFlow()
    var recentlyDeletedScribble: ScribbleEntity? = null

    private var currentJob: Job? = null

    init {
        collectScribbles(SortType.TIMESTAMP)
    }

    private fun collectScribbles(sortType: SortType) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val flow = when (sortType) {
                SortType.TITLE -> repository.getAllScribblesByTitle()
                SortType.TIMESTAMP -> repository.getAllScribblesByTimeStamp()
            }

            flow.distinctUntilChanged().collect { list ->
                _scribbleState.update {
                    it.copy(
                        scribbles = list
                    )
                }
            }
        }
    }

    fun onEvent(event: ScribbleUiEvent) {
        when (event) {
            is ScribbleUiEvent.SaveScribble -> {
                val title = event.title.trim()
                val content = event.content.trim()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModelScope.launch {
                        repository.insertScribble(
                            ScribbleEntity(
                                title = title,
                                content = content,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("Scribble saved"))
                        _scribbleState.update {
                            it.copy(
                                isDialogVisible = false,
                                currentTitle = "",
                                currentContent = ""
                            )
                        }
                    }
                }
            }

            is ScribbleUiEvent.DeleteScribble -> {
                viewModelScope.launch {
                    val deleted = repository.getScribbleById(event.id)
                    deleted?.let {
                        recentlyDeletedScribble = it
                        repository.deleteScribble(event.id)
                        _scribbleEventFlow.emit(
                            ScribbleUiEvent.ShowSnackBar(
                                message = "Scribble deleted",
                                action = "Undo"
                            )
                        )
                    }

                }
            }

            ScribbleUiEvent.DeleteAllScribbles -> {
                viewModelScope.launch {
                    if (repository.getAllScribblesByTitle().first().isNotEmpty()) {
                        repository.deleteAllScribbles()
                        _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("Deleted all scribbles"))
                    } else {
                        _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("No scribbles to delete"))
                    }
                }
            }

            is ScribbleUiEvent.SortScribbles -> {
                collectScribbles(event.sortType)
            }

            ScribbleUiEvent.ShowDialog -> {
                _scribbleState.update { it.copy(isDialogVisible = true) }
            }

            ScribbleUiEvent.HideDialog -> {
                _scribbleState.update { it.copy(isDialogVisible = false) }
            }

            is ScribbleUiEvent.ShowSnackBar -> {
                viewModelScope.launch {
                    _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar(event.message))
                }
            }

            is ScribbleUiEvent.UndoDelete -> {
                viewModelScope.launch {
                     repository.insertScribble(event.scribble)
                    _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("Scribble restored"))
                }

            }

        }
    }
}
