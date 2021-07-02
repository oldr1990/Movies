package com.example.mymovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovies.data.Constants.ERROR_UNKNOWN
import com.example.mymovies.models.Search
import com.example.mymovies.interfaces.OmdbAPI
import com.example.mymovies.util.Resource
import com.example.mymovies.models.DetailsData
import com.example.mymovies.models.Movie
import com.example.mymovies.ui.MainScreen.SearchPagingSource
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: OmdbAPI,
) : Repository {

    @ExperimentalPagingApi
    override suspend fun searchMovies(
        search: Search
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(10)
        ) {
           SearchPagingSource(search, api)
        }.flow
    }

    override suspend fun getMovie(id: String): Resource<DetailsData> {
        return try {
            val response = api.getMovieDetails(id)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                return Resource.Success(result)
            } else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: ERROR_UNKNOWN)
        }
    }
}