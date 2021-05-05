package com.example.test_movies.da


import android.os.Parcelable
import androidx.palette.graphics.Palette
import com.example.mymovies.data.MovieSwatch
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Movie(
    @SerializedName("Title")
    val name: String= "no data",
    @SerializedName("Poster")
    val imgURL: String= "no data",
    @SerializedName("Year")
    val year: String= "no data",
    @SerializedName("Type")
    val type: String= "no data",
    @SerializedName("imdbID")
    val imbdID: String = "",
    var colorBackground : Int = 0,
    var colorTextBackground : Int = 0,
    var colorText: Int = 0,

) : Parcelable
