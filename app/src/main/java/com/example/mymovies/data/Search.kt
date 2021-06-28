package com.example.mymovies.data

data class Search(
    var search: String,
    var page: Int? = null,
    var type: String? = Constants.TypeOfSearch.ALL_TYPES,
    var year: String? = null
)
