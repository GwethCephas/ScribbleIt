package com.ceph.scribbleit.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.domain.repository.ScribbleRepository
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiEvent
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiState
import com.ceph.scribbleit.presentation.scribbles.SortType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ScribbleRepository
) : ViewModel() {

    private val _scribbleState = MutableStateFlow(ScribbleUiState())
    val scribbleState = _scribbleState.asStateFlow()

    private val _scribbleEventFlow = MutableSharedFlow<ScribbleUiEvent>()
    val scribbleEventFlow = _scribbleEventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getAllScribblesByTimeStamp().collect { list ->
                _scribbleState.update { it.copy(scribbles = list) }
            }
        }
    }


    fun onEvent(event: ScribbleUiEvent) {
        when (event) {
            ScribbleUiEvent.DeleteAllScribbles -> {
                viewModelScope.launch {
                    repository.deleteAllScribbles()
                    _scribbleState.update {
                        it.copy(
                            scribbles = emptyList()
                        )
                    }
                    _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("All scribbles deleted"))
                }
            }

            is ScribbleUiEvent.DeleteScribble -> {
                viewModelScope.launch {
                    repository.deleteScribble(event.id)
                    _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("Scribble deleted"))
                }
            }

            ScribbleUiEvent.HideDialog -> {
                _scribbleState.update {
                    it.copy(
                        isDialogVisible = false
                    )
                }
            }

            ScribbleUiEvent.LoadScribbles -> {
                viewModelScope.launch {
                    repository.getAllScribblesByTimeStamp().collect { scribbles ->
                        _scribbleState.value = _scribbleState.value.copy(
                            scribbles = scribbles
                        )
                    }

                }
            }

            is ScribbleUiEvent.SaveScribble -> {
                if (event.title.isNotBlank() && event.content.isNotBlank()) {
                    viewModelScope.launch {
                        repository.insertScribble(
                            ScribbleEntity(
                                title = event.title,
                                content = event.content,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar("Scribble Saved"))
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

            ScribbleUiEvent.ShowDialog -> {
                _scribbleState.update {
                    it.copy(
                        isDialogVisible = true
                    )
                }
            }

            is ScribbleUiEvent.ShowSnackBar -> {
                viewModelScope.launch {
                    _scribbleEventFlow.emit(ScribbleUiEvent.ShowSnackBar(event.message))
                }

            }

            is ScribbleUiEvent.SortScribbles -> {
                when (event.sortType) {
                    SortType.TITLE -> {
                        viewModelScope.launch {
                            repository.getAllScribblesByTitle().collect { scribbles ->
                                _scribbleState.value = _scribbleState.value.copy(
                                    scribbles = scribbles
                                )
                            }

                        }
                    }

                    SortType.TIMESTAMP -> {
                        viewModelScope.launch {
                            repository.getAllScribblesByTimeStamp().collect { scribbles ->
                                _scribbleState.value = _scribbleState.value.copy(
                                    scribbles = scribbles
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}