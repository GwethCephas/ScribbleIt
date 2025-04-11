package com.ceph.scribbleit.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ScribbleDao {

    @Upsert
    suspend fun insertScribble(scribbleEntity: ScribbleEntity)

    @Query("SELECT * FROM scribbles ORDER BY timestamp DESC")
    fun getAllScribblesByTimeStamp(): Flow<List<ScribbleEntity>>

    @Query("SELECT * FROM scribbles ORDER BY title ASC")
    fun getAllScribblesByTitle(): Flow<List<ScribbleEntity>>

    @Query("DELETE FROM scribbles WHERE id = :id")
    suspend fun deleteScribble(id: Int)

    @Query("DELETE  FROM scribbles")
    suspend fun deleteAllScribbles()

}