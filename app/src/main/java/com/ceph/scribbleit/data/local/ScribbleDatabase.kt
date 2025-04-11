package com.ceph.scribbleit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScribbleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScribbleDatabase: RoomDatabase() {
    abstract val dao: ScribbleDao
}