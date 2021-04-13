package com.example.test_movies.da


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("Title")
    val name: String= "no data",
    @SerializedName("Poster")
    val imgURL: String= "no data",
    @SerializedName("Year")
    val year: String= "no data",
    @SerializedName("Type")
    val type: String= "no data"
) : Parcelable
