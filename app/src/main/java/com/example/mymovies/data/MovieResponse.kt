package com.example.test_movies.da

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val movies: List<Movie>,
    @SerializedName("totalResults")
    val totalResult: Int
)
