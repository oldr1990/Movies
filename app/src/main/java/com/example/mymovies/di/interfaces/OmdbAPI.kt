package com.example.mymovies.di.interfaces

import com.example.mymovies.models.DetailsData
import com.example.mymovies.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbAPI {
    
	//поиск фильмов/сериалов
	@GET("/")
    suspend fun getAllMovies(
        @Query("s") search: String,
        @Query("page") page: Int?,
        @Query("type") type: String?,
        @Query("y") year: String?,
        @Query("apikey") apiKey: String = "830086",
    ): Response<MovieResponse>
	
	//запрос детальной информации о фильме/сериале
    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") search: String,
        @Query("apikey") apiKey: String = "830086"
    ):Response<DetailsData>
}