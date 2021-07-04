package com.example.mymovies.ui.MainScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mymovies.utils.MainCoroutineRule
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.models.Search
import com.example.mymovies.utils.FakeDefaultRepository
import com.example.mymovies.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi

class SearcherViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearcherViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearcherViewModel(FakeDefaultRepository(), TestDispatcherProvider())
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun properlyTestSearchingOk()= runBlockingTest {
        var answer = true
       runBlocking {   viewModel.initPaging(Search("one"))}
        answer = viewModel.pagingFlow != null
        assertThat(answer).isTrue()
    }
    @Test
    fun unProperlyTestSearchingError()= runBlockingTest {
        var answer = true
       runBlocking {   viewModel.initPaging(Search(EMPTY_STRING))}
        answer = viewModel.pagingFlow == null
        assertThat(answer).isTrue()
    }

}