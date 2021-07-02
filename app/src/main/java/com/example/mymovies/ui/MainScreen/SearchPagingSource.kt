package com.example.mymovies.ui.MainScreen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovies.models.Search
import com.example.mymovies.interfaces.OmdbAPI
import com.example.mymovies.models.Movie

class SearchPagingSource(
    private val search: Search,
    private val api: OmdbAPI
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageIndex = params.key ?: 1
            search.page = pageIndex
            val response = api.getAllMovies(search.search,pageIndex, search.type,search.year)
            val responseData  = mutableListOf<Movie>()
            val data = response.body()?.movies ?: emptyList()
            responseData.addAll(data)
            val prevKey  = if (pageIndex == 1) null else pageIndex - 1
            LoadResult.Page(responseData, prevKey = prevKey, pageIndex.plus(1))
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}