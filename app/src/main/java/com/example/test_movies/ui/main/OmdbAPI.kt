package com.example.test_movies.ui.main

import com.example.test_movies.da.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbAPI {
    @GET("/")
    suspend fun getAllMovies(
        @Query("s") search: String,
        @Query("apikey") apiKey: String = "830086"
    ): Response<MovieResponse>
}