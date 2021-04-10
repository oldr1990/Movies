package com.example.test_movies.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.test_movies.da.Movie
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    val movieData: MutableLiveData<List<Movie>> = MutableLiveData()

    fun start(search: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
        viewModelScope.launch {
            val result = api.getAllMovies(search)
            result.body()?.movies?.let {
                movieData.postValue(it)
            }
        }
    }
}