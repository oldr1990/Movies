package com.example.mymovies.models

import com.example.mymovies.data.Constants

data class Search(
    var search: String,
    var page: Int? = null,
    var type: String? = Constants.TypeOfSearch.ALL_TYPES,
    var year: String? = null
)
