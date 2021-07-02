package com.example.mymovies.data.repository

import androidx.paging.PagingData
import com.example.mymovies.models.Search
import com.example.mymovies.util.Resource
import com.example.mymovies.models.DetailsData
import com.example.mymovies.models.Movie
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun searchMovies( search: Search): Flow<PagingData<Movie>>
    suspend fun getMovie(id: String):Resource<DetailsData>
}