package com.example.test_movies.da

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val movies: MutableList<Movie>,
    @SerializedName("totalResults")
    val totalResult: Int,
    @SerializedName("Response")
    val Response: String,
    @SerializedName("Error")
    val Error: String?
)
