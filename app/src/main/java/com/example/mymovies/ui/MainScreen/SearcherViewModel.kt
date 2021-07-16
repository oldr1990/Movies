package com.example.mymovies.ui.MainScreen


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.models.Search
import com.example.mymovies.data.repository.Repository
import com.example.mymovies.util.DispatcherProvider
import com.example.mymovies.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SearcherViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    var pagingFlow: Flow<PagingData<Movie>>? = null
    var searchSetup = Search(EMPTY_STRING)
    var spinnerPosition = 0
    var isLoading = false

    fun initPaging(search: Search) {
        viewModelScope.launch(dispatcher.default) {
            if (search.search != EMPTY_STRING) {
                isLoading = true
                pagingFlow = repository.searchMovies(search).cachedIn(viewModelScope)
            }
        }
    }
}