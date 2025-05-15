package com.ceph.scribbleit.viewmodel

import app.cash.turbine.test
import com.ceph.scribbleit.data.local.ScribbleEntity
import com.ceph.scribbleit.presentation.homeScreen.HomeViewModel
import com.ceph.scribbleit.presentation.scribbles.ScribbleUiEvent
import com.ceph.scribbleit.repository.FakeScribbleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: FakeScribbleRepository
    private lateinit var homeViewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeScribbleRepository()
        homeViewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save scribbles emit snackBar and clear fields`() = testScope.runTest {
        val event = ScribbleUiEvent.SaveScribble("Test Title", "Test Content")

        val job = launch {
            homeViewModel.scribbleEventFlow.test {
                homeViewModel.onEvent(event)

                val emitted = awaitItem()
                assert(emitted is ScribbleUiEvent.ShowSnackBar)
                assertEquals("Scribble saved", (emitted as ScribbleUiEvent.ShowSnackBar).message)

                cancelAndIgnoreRemainingEvents()
            }
        }

        advanceUntilIdle()

        val state = homeViewModel.scribbleState.value

        assertEquals("", state.currentTitle)
        assertEquals("", state.currentContent)
        assertEquals(1, repository.getAllScribblesByTitle().first().size)

        job.join()
    }

    @Test
    fun `delete all scribbles updates state and emits snackBar`() = testScope.runTest {
        val scribble = ScribbleEntity(
            title = "Test Title",
            content = "Test Content",
            timestamp = System.currentTimeMillis()
        )
        repository.insertScribble(scribble)

        val inserted = repository.getAllScribblesByTitle().first().first()

        val job = launch {
            homeViewModel.scribbleEventFlow.test {
                homeViewModel.onEvent(ScribbleUiEvent.DeleteScribble(inserted.id))

                val emitted = awaitItem()
                assert(emitted is ScribbleUiEvent.ShowSnackBar)
                assertEquals("Scribble deleted", (emitted as ScribbleUiEvent.ShowSnackBar).message)
                assertEquals("Undo", emitted.action)

                cancelAndIgnoreRemainingEvents()
            }
        }

        advanceUntilIdle()

        val scribbles = repository.getAllScribblesByTitle().first()
        assertEquals(0, scribbles.size)

        job.join()


    }


}