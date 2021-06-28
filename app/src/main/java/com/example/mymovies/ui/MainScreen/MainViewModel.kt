package com.example.mymovies.ui.MainScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.interfaces.OmdbAPI
import com.example.test_movies.da.Movie
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import java.util.*

class MainViewModel : ViewModel() {
    val movieData: MutableLiveData<List<Movie>> = MutableLiveData()
    val numberOfResult: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Boolean> = MutableLiveData()
    val wrongRequest: MutableLiveData<Boolean> = MutableLiveData()
    var currentType: Int = 0
    var currentPage: Int = 1
    var currentYear: String? = null

    fun getData(search: String, year: String, typeSelected: String, page: Int = 1) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbAPI::class.java)
        Log.e("!@#", "GetData  was called")
        viewModelScope.launch {
            var type: String? = typeSelected.toLowerCase(Locale.ROOT)
            if (type == "all types") type = null
            currentYear = year
            try {
                val result = api.getAllMovies(search, page, type, year)
                Log.e("!@#", "result called")
                result?.body()?.let {
                    Log.e("!@#", "ViewModel result called")
                    if (it.Response == "True") {
                        numberOfResult.postValue(it.totalResult)
                        movieData.postValue(it.movies)
                        if (errorMessage.value != true) errorMessage.postValue(true)
                        if (wrongRequest.value != false) wrongRequest.postValue(false)
                    }
                    else wrongRequest.postValue(true)
                }
            } catch (e: UnknownHostException) {
                //ловим отсутсвие интернета
                errorMessage.postValue(false)
            }
        }
    }
}