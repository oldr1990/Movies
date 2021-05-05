package com.example.mymovies

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(){
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }
    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        val types = resources.getStringArray(R.array.type)
        if (search_type_spinner != null){
            val arrayAdapter = ArrayAdapter(requireContext(),  R.layout.spinner_item, types)
            search_type_spinner.adapter = arrayAdapter
            search_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.type = types[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recylerview?.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.movieData.observe(viewLifecycleOwner,{
            recylerview.adapter = MovieAdapter(it)
        })
        search_button.setOnClickListener {
            viewModel.getData(input_text.text.toString())
            viewModel.movieData.observe(viewLifecycleOwner, {
                recylerview.adapter = MovieAdapter(it)
            })
        }
    }
}