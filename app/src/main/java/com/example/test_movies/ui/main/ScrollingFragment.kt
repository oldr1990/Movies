package com.example.test_movies.ui.main

import android.os.Bundle
import android.os.Parcel
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_movies.R
import com.example.test_movies.da.DetailsData
import com.example.test_movies.da.Movie
import kotlinx.android.synthetic.main.fragment_scrolling.*

class ScrollingFragment : Fragment() {
    companion object {
        fun newInstance():ScrollingFragment{
            return ScrollingFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scrolling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var movie =  Movie()
        // movie = arguments?.getParcelable<Movie>("My_movie")
       // descriptionTitleTextView.setText("some_title")
    }



}