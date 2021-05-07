package com.example.mymovies.interfaces

import com.example.test_movies.da.DetailsData
import com.example.test_movies.da.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbAPI {
    @GET("/")
    suspend fun getAllMovies(
        @Query("s") search: String,
        @Query("page") page: Int?,
        @Query("type") type: String?,
        @Query("y") year: String?,
        @Query("apikey") apiKey: String = "830086",

    ): Response<MovieResponse>
    @GET("/")
    suspend fun getMovie(
        @Query("i") search: String,
        @Query("apikey") apiKey: String = "830086"
    ):Response<DetailsData>
}