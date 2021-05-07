package com.example.mymovies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.interfaces.OmdbAPI
import com.example.test_movies.da.Movie
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    val movieData: MutableLiveData<List<Movie>> = MutableLiveData()
    val numberOfResult: MutableLiveData<Int> = MutableLiveData()
    val response: MutableLiveData<String> = MutableLiveData()
    var currentType: Int = 0
    var currentPage: Int = 1
    var currentYear: String? = null

    fun getData(search: String, year: String, typeSelected: String, page: Int = 1) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
        viewModelScope.launch {
            var type: String? = typeSelected.toLowerCase()
            if (type == "all types") type = null
            currentYear = year
            val result = api.getAllMovies(search, page, type, year)
            result?.body()?.let {
                numberOfResult.postValue(it.totalResult)
                movieData.postValue(it.movies)
                response.postValue(it.Response)
            }
        }
    }
}