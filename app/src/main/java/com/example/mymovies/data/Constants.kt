package com.example.mymovies.data

object Constants {
     const val BASE_URL = "http://www.omdbapi.com/"
     const val ERROR_UNKNOWN = "Неизвестная ошибка!"
     const val EMPTY_STRING = ""
     const val EMPTY_SEARCH_WARNING = "Строка поиска не должна быть пустой!"
     const val BACK_PRESSED_MESSAGE = "Для выхода нажмите назад еще раз"

     object TypeOfSearch{
           val ALL_TYPES = null
          const val MOVIES = "movie"
          const val SERIES = "series"
     }
}