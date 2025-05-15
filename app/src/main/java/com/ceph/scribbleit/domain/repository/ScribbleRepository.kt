package com.ceph.scribbleit.domain.repository

import com.ceph.scribbleit.data.local.ScribbleEntity
import kotlinx.coroutines.flow.Flow

interface ScribbleRepository {

    suspend fun insertScribble(scribbleEntity: ScribbleEntity)

    suspend fun deleteScribble(id: Int)

    suspend fun deleteAllScribbles()

    suspend fun getScribbleById(id: Int): ScribbleEntity?

    fun getAllScribblesByTimeStamp(): Flow<List<ScribbleEntity>>

    fun getAllScribblesByTitle(): Flow<List<ScribbleEntity>>



}