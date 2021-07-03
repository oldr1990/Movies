package com.example.mymovies.ui.DetailScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mymovies.MainCoroutineRule
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.utils.FakeDefaultRepository
import com.example.mymovies.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(FakeDefaultRepository(), TestDispatcherProvider())
    }


    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun myTest() = runBlockingTest {
        assertThat(false).isFalse()
    }

    @Test
    fun getDetailsDataWithEmptyIDError() = runBlockingTest {
        var answer = false
        runBlocking {
            viewModel.getData(EMPTY_STRING)
        }
        answer = when (viewModel.detailResponse.value) {
            DetailViewModel.DetailsEvent.Empty -> false
            is DetailViewModel.DetailsEvent.Failure -> true
            DetailViewModel.DetailsEvent.Loading -> false
            is DetailViewModel.DetailsEvent.Success -> false
        }
        assertThat(answer).isTrue()
    }

    @Test
    fun orderOfStatesTrue() =
        runBlockingTest {
            var answer = true
            when (viewModel.detailResponse.value) {
                DetailViewModel.DetailsEvent.Empty -> {
                }
                is DetailViewModel.DetailsEvent.Failure -> answer = false
                DetailViewModel.DetailsEvent.Loading -> answer = false
                is DetailViewModel.DetailsEvent.Success -> answer = false
            }
            viewModel.getData("123456789")
            when (viewModel.detailResponse.value) {
                DetailViewModel.DetailsEvent.Empty -> answer = false
                is DetailViewModel.DetailsEvent.Failure -> answer = false
                DetailViewModel.DetailsEvent.Loading -> answer = false
                is DetailViewModel.DetailsEvent.Success -> {
                }
            }
            assertThat(answer).isEqualTo(true)

        }


    @Test
    fun getDetailsDataWithSuccess() = runBlockingTest {
        var answer = true
        runBlocking { viewModel.getData("123456789") }
        when (viewModel.detailResponse.value) {
            DetailViewModel.DetailsEvent.Empty -> answer = false
            is DetailViewModel.DetailsEvent.Failure -> answer = false
            DetailViewModel.DetailsEvent.Loading -> answer = false
            is DetailViewModel.DetailsEvent.Success -> {
            }
        }
        assertThat(answer).isTrue()
    }

}