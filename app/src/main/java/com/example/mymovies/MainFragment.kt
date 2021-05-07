package com.example.mymovies

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.AnyRes
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.new_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainFragment : Fragment() {
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

    @SuppressLint("ResourceType", "SetTextI18n", "ShowToast")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        spinnerSetup()
        updateUI()
        recyclerview?.layoutManager = LinearLayoutManager(requireContext())

        search_button.setOnClickListener {
            viewModel.currentPage = 1
            requestData()
        }
    }
    //Запрашивает данные с сервера, в случае успуха выводит
    private fun requestData() {
        Log.e("!@#", "requestData was called")
        viewModel.response.removeObservers(LifecycleOwner {  })
            viewModel.getData(
                input_text.text.toString(),
                editTextYear.text.toString(),
                search_type_spinner.selectedItem.toString(),
                viewModel.currentPage
            )
        viewModel.response.observe(viewLifecycleOwner, {
            Log.e("!@#", "response  $it")
            if (it == "True") {
                spinnerSetup()
                updateUI()
            }
        })
    }

    private fun spinnerSetup() {
        viewModel?.currentYear?.let {
            editTextYear.setText(it)
        }
        search_type_spinner.setSelection(viewModel.currentType)
        val types = resources.getStringArray(R.array.type)
        if (search_type_spinner != null) {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, types)
            search_type_spinner.adapter = arrayAdapter
            search_type_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.currentType = position
                        Log.e("!@#", "id = ${position}")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        viewModel.numberOfResult.observe(viewLifecycleOwner, {
            if (it != null && it != 0) {
                val pages = when (it % 10) {
                    0 -> it / 10
                    in 1..9 -> it / 10 + 1
                    else -> 1
                }
                textViewResultNumber.text =
                    resources.getString(R.string.result_of_searching_label) + it.toString()
                cardViewResultNumber.visibility = CardView.VISIBLE
                bottomNavigation(viewModel.currentPage, pages)
            }
        })
        Log.e("!@#" , "updateUI was called!")
        viewModel.movieData.observe(viewLifecycleOwner, {
            it?.let { recyclerview.adapter = MovieAdapter(it) }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun bottomNavigation(currentPage: Int, pages: Int) {
        if (pages >= 2) {
            textViewPageNavigation.text = "$currentPage/$pages pages"
            cardViewBottomNavigation.visibility = CardView.VISIBLE
            if (currentPage > 1) {
                pageFirstPageNavigation.setTextColor(Color.BLACK)
                pageBackNavigation.setTextColor(Color.BLACK)
                pageFirstPageNavigation.setOnClickListener {
                    viewModel.currentPage = 1
                    requestData()
                }
                pageBackNavigation.setOnClickListener {
                    viewModel.currentPage--
                    requestData()
                }
            } else {
                pageFirstPageNavigation.setTextColor(Color.LTGRAY)
                pageBackNavigation.setTextColor(Color.LTGRAY)
                pageBackNavigation.isClickable = false
                pageFirstPageNavigation.isClickable = false
            }
            if (currentPage < pages) {
                pageNextPageNavigation.setTextColor(Color.BLACK)
                pageLastPageNavigation.setTextColor(Color.BLACK)
                pageNextPageNavigation.setOnClickListener {
                    viewModel.currentPage++
                    requestData()
                }
                pageLastPageNavigation.setOnClickListener {
                    viewModel.currentPage = pages
                    requestData()
                }
            } else {
                pageNextPageNavigation.setTextColor(Color.LTGRAY)
                pageLastPageNavigation.setTextColor(Color.LTGRAY)
                pageNextPageNavigation.isClickable = false
                pageLastPageNavigation.isClickable = false
            }
        }
        else cardViewBottomNavigation.visibility = CardView.GONE


    }
}