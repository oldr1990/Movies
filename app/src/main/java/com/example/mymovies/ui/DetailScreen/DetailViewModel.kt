package com.example.mymovies.ui.DetailScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovies.interfaces.OmdbAPI
import com.example.test_movies.da.DetailsData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class DetailViewModel : ViewModel() {
    var isFailed: MutableLiveData<Boolean> = MutableLiveData()
    var movieDetails: DetailsData = DetailsData()

    suspend fun getData(id: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
        try {
            val result = api.getMovieDetails(id)
            result?.body()?.let {
                   Log.e("!@#", it.toString())
                   movieDetails = it
                isFailed.postValue(false)
            }
        } catch (e: UnknownHostException) {
			//ловим отсутсвие интернета
            isFailed.postValue(true)
        }
    }
}