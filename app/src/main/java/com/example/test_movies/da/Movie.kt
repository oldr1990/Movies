package com.example.test_movies.da

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("Title")
    val name: String,
    @SerializedName("Poster")
    val imgURL: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Type")
    val type: String
)
