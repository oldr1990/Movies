package com.example.mymovies.repository

import com.example.mymovies.data.Search
import com.example.mymovies.util.Resource
import com.example.test_movies.da.DetailsData
import com.example.test_movies.da.MovieResponse
import retrofit2.http.Query

interface Repository {
    suspend fun searchMovies( search: Search,):Resource<MovieResponse>
    suspend fun getMovie(id: String):Resource<DetailsData>
}