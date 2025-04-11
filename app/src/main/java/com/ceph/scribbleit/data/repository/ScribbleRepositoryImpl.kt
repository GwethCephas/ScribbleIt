package com.ceph.scribbleit.data.repository

import android.util.Log
import com.ceph.scribbleit.data.local.ScribbleDatabase
import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.domain.repository.ScribbleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


class ScribbleRepositoryImpl(
    database: ScribbleDatabase
) : ScribbleRepository {

    private val dao = database.dao

    override suspend fun insertScribble(scribbleEntity: ScribbleEntity) {
        dao.insertScribble(scribbleEntity)
    }

    override suspend fun deleteScribble(id: Int) {
        dao.deleteScribble(id)
    }

    override suspend fun deleteAllScribbles() {
        dao.deleteAllScribbles()
    }

    override fun getAllScribblesByTimeStamp(): Flow<List<ScribbleEntity>> {
        return dao.getAllScribblesByTimeStamp()
            .catch { e ->
                Log.d("ScribbleRepositoryImpl", "getAllScribblesByTimeStamp: ${e.message}")
            }
    }

    override fun getAllScribblesByTitle(): Flow<List<ScribbleEntity>> {
        return dao.getAllScribblesByTitle()
            .catch { e ->
                Log.d("ScribbleRepositoryImpl", "getAllScribblesByTitle: ${e.message}")
            }
    }

}