package com.example.mymovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.MovieSwatch
import com.example.mymovies.interfaces.OmdbAPI
import com.example.test_movies.da.Movie
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    val movieData: MutableLiveData<List<Movie>> = MutableLiveData()
    val numberOfResult: MutableLiveData<Int> = MutableLiveData()
    var type: String? = null
     init {

     }fun getData(search: String, page: Int = 1){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
         viewModelScope.launch {
             if (type == "All types") type = null
            val result = api.getAllMovies(search, page, type)
             result?.body()?.totalResult?.let {
                 numberOfResult.postValue(it)
             }
            result?.body()?.movies?.let {
            movieData.postValue(it)
            }
         }
    }
}