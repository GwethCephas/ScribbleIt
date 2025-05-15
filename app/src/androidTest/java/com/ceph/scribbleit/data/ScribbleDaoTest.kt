package com.ceph.scribbleit.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ceph.scribbleit.data.local.ScribbleDao
import com.ceph.scribbleit.data.local.ScribbleDatabase
import com.ceph.scribbleit.data.local.ScribbleEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ScribbleDaoTest {

    private lateinit var database: ScribbleDatabase
    private lateinit var dao: ScribbleDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ScribbleDatabase::class.java
        ).build()
        dao = database.dao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetScribble() = runTest {

        val scribble = ScribbleEntity(
            id = 1,
            title = "Test",
            content = "Test content",
            timestamp = System.currentTimeMillis()
        )
        dao.insertScribble(scribble)

        val result = dao.getAllScribblesByTimeStamp().first()

        assertEquals(1, result.size)
        assertEquals("Test", result[0].title)

    }

    @Test
    fun deleteScribble() = runTest {
        val scribble = ScribbleEntity(
            id = 1,
            title = "Test",
            content = "Test content",
            timestamp = System.currentTimeMillis()
        )
        dao.insertScribble(scribble)
        dao.deleteScribble(scribble.id)

        assertEquals(0, dao.getAllScribblesByTimeStamp().first().size)
    }
}


















