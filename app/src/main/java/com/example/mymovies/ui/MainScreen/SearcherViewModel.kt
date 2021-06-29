package com.example.mymovies.ui.MainScreen


import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.data.Constants.ERROR_UNKNOWN
import com.example.mymovies.data.Search
import com.example.mymovies.repository.Repository
import com.example.mymovies.util.DispatcherProvider
import com.example.mymovies.util.Resource
import com.example.test_movies.da.MovieResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearcherViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _searchResponse = MutableStateFlow<SearchEvent>(SearchEvent.Empty)
    val searchResponse: StateFlow<SearchEvent> = _searchResponse
    var page = 0
    var searchSetup = Search(EMPTY_STRING)
    var spinnerPosition = 0
    var isLoading = View.GONE
    var isClickable = true

    sealed class SearchEvent {
        class Success(val response: MovieResponse) : SearchEvent()
        class Failure(val errorText: String) : SearchEvent()
        object Loading : SearchEvent()
        object Empty : SearchEvent()
    }

    fun searchMovies(
        search: Search
    ) {
        viewModelScope.launch(dispatcher.io) {
            _searchResponse.value = SearchEvent.Loading
            val response = repository.searchMovies(search)
            when (response) {
                is Resource.Error -> {
                    _searchResponse.value = SearchEvent.Failure(response.message.toString())
                }
                is Resource.Success -> {
                    if (response.data?.Response.toBoolean()) {
                        response.data?.let {
                            _searchResponse.value = SearchEvent.Success(response.data)
                        }
                    } else {
                        _searchResponse.value = SearchEvent.Failure(response.data?.Error?: ERROR_UNKNOWN)
                    }
                }
            }

        }
    }
}