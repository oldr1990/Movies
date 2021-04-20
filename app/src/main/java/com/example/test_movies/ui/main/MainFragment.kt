package com.example.test_movies.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_movies.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recList?.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        searchButton.setOnClickListener(){
            viewModel.start(textInputEditText.text.toString())
            viewModel.movieData.observe(viewLifecycleOwner, Observer {
                recList.adapter = MovieAdapter(it)
            })

        }
    }

    interface OnItemSelected{
        fun itemTitle(text: String)
    }
}
