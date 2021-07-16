package com.example.mymovies.ui.MainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.mymovies.R
import com.example.mymovies.ui.animations.WaitingAnimation
import com.example.mymovies.data.Constants
import com.example.mymovies.data.Constants.EMPTY_SEARCH_WARNING
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.databinding.MainFragmentBinding
import com.example.mymovies.ui.adapters.PagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.new_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var waitingAnimation: WaitingAnimation
    private lateinit var binding: MainFragmentBinding
    private val _binding get() = binding
    private val hiltViewModel: SearcherViewModel by viewModels()
    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return _binding.root

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backgroundImageMain.load(R.drawable.movie_info_searcher) {
            scale(Scale.FILL)
        }
        //Initialisation
        pagingAdapter = PagingAdapter()
        recyclerview.adapter = pagingAdapter
        waitingAnimation = WaitingAnimation(imageViewWaiting, cardViewWaitingAnimation)
        spinnerSetup()
        recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        _binding.searchButton.isClickable = !hiltViewModel.isLoading
        hiltViewModel.pagingFlow?.let {
            CoroutineScope(Dispatchers.IO).launch {
                it.collect {
                    pagingAdapter.submitData(it)
                }
            }
        }
        //Loading state collect
        viewLifecycleOwner.lifecycleScope.launch {
            try {  pagingAdapter.loadStateFlow.collectLatest { state->
                    when (state.refresh) {
                        is LoadState.NotLoading -> {
                            hiltViewModel.isLoading = false
                            waitingAnimation.turnOffAnimation()
                        }
                        LoadState.Loading -> {
                            hiltViewModel.isLoading = false
                            waitingAnimation.turnOnAnimation()
                        }
                        is LoadState.Error -> {
                            hiltViewModel.isLoading = false
                            waitingAnimation.turnOffAnimation()
                        }
                    }
                }

            } catch (e: Exception){
                Toast.makeText(requireContext(),  e.message?:"Error", Toast.LENGTH_SHORT).show()
            }

        }
        // Search button on click listener
        search_button.setOnClickListener { view ->
            if (input_text.text.toString() == EMPTY_STRING) {
                Toast.makeText(view.context, EMPTY_SEARCH_WARNING, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (editTextYear.text.toString() != EMPTY_STRING)
                hiltViewModel.searchSetup.year = editTextYear.text.toString()
            hiltViewModel.searchSetup.type = when (hiltViewModel.spinnerPosition) {
                0 -> Constants.TypeOfSearch.ALL_TYPES
                1 -> Constants.TypeOfSearch.MOVIES
                2 -> Constants.TypeOfSearch.SERIES
                else -> Constants.TypeOfSearch.ALL_TYPES
            }
            hiltViewModel.searchSetup.search = input_text.text.toString()
            recyclerview.apply {
                lifecycleScope.launch(Dispatchers.IO) {
                    hiltViewModel.initPaging(hiltViewModel.searchSetup)
                    hiltViewModel.pagingFlow?.collect {data->
                        pagingAdapter.submitData(data)
                    }
                }
            }

        }
    }

    private fun spinnerSetup() {
        search_type_spinner.setSelection(hiltViewModel.spinnerPosition)
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
                        hiltViewModel.spinnerPosition = position
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }
    }
}
