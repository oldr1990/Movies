package com.example.mymovies.ui.DetailScreen


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.repository.Repository
import com.example.mymovies.util.DispatcherProvider
import com.example.mymovies.util.Resource
import com.example.test_movies.da.DetailsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DetailViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _detailResponse = MutableStateFlow<DetailsEvent>(DetailsEvent.Empty)
    val detailResponse: StateFlow<DetailsEvent> = _detailResponse

    sealed class DetailsEvent {
        class Success(val response: DetailsData) : DetailsEvent()
        class Failure(val errorText: String) : DetailsEvent()
        object Loading : DetailsEvent()
        object Empty : DetailsEvent()
    }

     fun getData(id: String) {
        viewModelScope.launch(dispatcher.io) {
            _detailResponse.value = DetailsEvent.Loading
            when (val response = repository.getMovie(id)) {
                is Resource.Error -> {
                    _detailResponse.value = DetailsEvent.Failure(response.message.toString())
                }
                is Resource.Success -> {
                    response.data?.let {
                        _detailResponse.value = DetailsEvent.Success(response = response.data)
                    }
                }
            }
        }
    }
}