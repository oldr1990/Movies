package com.example.mymovies.utils

import androidx.paging.PagingData
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.data.repository.Repository
import com.example.mymovies.models.DetailsData
import com.example.mymovies.models.Movie
import com.example.mymovies.models.Search
import com.example.mymovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AnrodiTestFakeDefaultRepository : Repository{

    override suspend fun searchMovies(search: Search): Flow<PagingData<Movie>> {
       return if (search.search == EMPTY_STRING ) flowOf(PagingData.empty())
           else
           flowOf(PagingData.from(listOf(Movie())))
    }

    override suspend fun getMovie(id: String): Resource<DetailsData> {
        return if(id == EMPTY_STRING) Resource.Error(String())
        else Resource.Success(DetailsData())
    }
}