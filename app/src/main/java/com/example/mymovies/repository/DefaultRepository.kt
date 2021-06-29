package com.example.mymovies.repository

import android.util.Log
import com.example.mymovies.data.Constants.ERROR_UNKNOWN
import com.example.mymovies.data.Search
import com.example.mymovies.interfaces.OmdbAPI
import com.example.mymovies.util.Resource
import com.example.test_movies.da.DetailsData
import com.example.test_movies.da.MovieResponse
import java.lang.Exception
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: OmdbAPI
) : Repository {

    override suspend fun searchMovies(
        search: Search
    ): Resource<MovieResponse> {
        return try {
            val response = api.getAllMovies(search.search, search.page, search.type, search.year)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                return Resource.Success(result)
            } else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: ERROR_UNKNOWN)
        }
    }

    override suspend fun getMovie(id: String): Resource<DetailsData> {
        return try {
            val response = api.getMovieDetails(id)
            val result = response.body()
            Log.e("!@#","SearchViewModel befor if")
            if (response.isSuccessful && result != null) {
                return Resource.Success(result)
            } else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: ERROR_UNKNOWN)
        }
    }
}