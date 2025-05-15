package com.ceph.scribbleit.repository

import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.domain.repository.ScribbleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeScribbleRepository: ScribbleRepository {


    private val scribbles = mutableListOf<ScribbleEntity>()
    private val getAllScribblesByTimeStamp = MutableStateFlow<List<ScribbleEntity>>(scribbles)
    private val getAllScribblesByTitle = MutableStateFlow<List<ScribbleEntity>>(scribbles)

    override suspend fun insertScribble(scribbleEntity: ScribbleEntity) {
        scribbles.add(scribbleEntity)
        getAllScribblesByTimeStamp.value = scribbles.toList()
        getAllScribblesByTitle.value = scribbles.toList()
    }

    override suspend fun deleteScribble(id: Int) {
        scribbles.removeAll { it.id == id }
        getAllScribblesByTimeStamp.value = scribbles.toList()
        getAllScribblesByTitle.value = scribbles.toList()
    }

    override suspend fun deleteAllScribbles() {
        scribbles.clear()
        getAllScribblesByTimeStamp.value = scribbles.toList()
        getAllScribblesByTitle.value = scribbles.toList()
    }

    override suspend fun getScribbleById(id: Int): ScribbleEntity? {
        return scribbles.first().takeIf { it.id == id }
    }

    override fun getAllScribblesByTimeStamp(): Flow<List<ScribbleEntity>> {
        return  getAllScribblesByTimeStamp.map { sortByTimeStamp -> sortByTimeStamp.sortedBy { it.timestamp } }
    }

    override fun getAllScribblesByTitle(): Flow<List<ScribbleEntity>> {
        return getAllScribblesByTitle.map { sortByTitle -> sortByTitle.sortedBy { it.title } }
    }
}