package com.example.test_movies.ui.main

import com.example.test_movies.da.DetailsData
import com.example.test_movies.da.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailsInterface {
    @GET("/")
    suspend fun getAllMovies(
        @Query("t") search: String,
        @Query("apikey") apiKey: String = "830086"
    ): Response<DetailsData>
}