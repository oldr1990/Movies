package com.example.mymovies

import androidx.lifecycle.ViewModel
import com.example.mymovies.interfaces.OmdbAPI
import com.example.test_movies.da.DetailsData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailViewModel : ViewModel() {
    var movieDetails: DetailsData = DetailsData()

    suspend fun getData(id: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
        val result = api.getMovie(id)
            result?.body()?.let {
                movieDetails = it
            }
    }
}