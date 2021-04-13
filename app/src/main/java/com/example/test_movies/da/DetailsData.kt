package com.example.test_movies.da

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsData(
    @SerializedName("Title")
    val name: String = "no data",
    @SerializedName("Year")
    val year: String = "no data",
    @SerializedName("Rated")
    val rated: String= "no data",
    @SerializedName("Released")
    val released: String= "no data",
    @SerializedName("Runtime")
    val runtime: String= "no data",
    @SerializedName("Genre")
    val type: String= "no data",
    @SerializedName("Director")
    val director: String= "no data",
    @SerializedName("Writer")
    val writer: String= "no data",
    @SerializedName("Actors")
    val actors: String= "no data",
    @SerializedName("Plot")
    val plot: String= "no data",
    @SerializedName("Language")
    val language: String= "no data",
    @SerializedName("Country")
    val country: String= "no data",
    @SerializedName("Awards")
    val awards: String= "no data",
    @SerializedName("Poster")
    val imgURL: String= "no data",
    @SerializedName("Metascore")
    val metascore: String= "no data",
    @SerializedName("imdbRating")
    val imdbRating: String= "no data",
    @SerializedName("imdbVotes")
    val imdbVotes: String= "no data",
    @SerializedName("imdbID")
    val imdbID: String= "no data",
    @SerializedName("BoxOffice")
    val cash: String= "no data",
    @SerializedName("Production")
    val Production: String= "no data",

):Parcelable
