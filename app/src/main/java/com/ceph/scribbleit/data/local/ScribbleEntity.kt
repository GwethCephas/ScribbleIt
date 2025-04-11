package com.ceph.scribbleit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("scribbles")
data class ScribbleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long
)
