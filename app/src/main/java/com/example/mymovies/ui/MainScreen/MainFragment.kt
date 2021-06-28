package com.example.mymovies.ui.MainScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.mymovies.ui.adapters.MovieAdapter
import com.example.mymovies.R
import com.example.mymovies.animations.WaitingAnimation
import com.example.mymovies.data.Constants
import com.example.mymovies.data.Constants.EMPTY_SEARCH_WARNING
import com.example.mymovies.data.Constants.EMPTY_STRING
import com.example.mymovies.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.new_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var waitingAnimation: WaitingAnimation
    private lateinit var binding: MainFragmentBinding
    private val hiltViewModel: SearcherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }


    @SuppressLint("ResourceType", "SetTextI18n", "ShowToast")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backgroundImageMain.load(R.drawable.movie_info_searcher) {
            scale(Scale.FILL)
        }
        binding.isLoading = hiltViewModel.isLoading
        waitingAnimation = WaitingAnimation(imageViewWaiting, cardViewWaitingAnimation)
        spinnerSetup()
        recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        //слушатель кнопки поиск
        CoroutineScope(Dispatchers.Main).launch {
            hiltViewModel.searchResponse.collect {
                when (it) {
                    SearcherViewModel.SearchEvent.Empty -> {
                        hiltViewModel.isLoading = View.GONE
                        hiltViewModel.isClickable = true
                        waitingAnimation.turnOffAnimation()
                        search_button.isClickable = true
                    }
                    is SearcherViewModel.SearchEvent.Failure -> {
                        hiltViewModel.isClickable = true
                        hiltViewModel.isLoading = View.GONE
                        waitingAnimation.turnOffAnimation()
                        Toast.makeText(view?.context, it.errorText, Toast.LENGTH_SHORT).show()
                    }
                    SearcherViewModel.SearchEvent.Loading -> {
                        disablingUI()
                        hiltViewModel.isClickable = false
                        hiltViewModel.isLoading = View.VISIBLE
                    }
                    is SearcherViewModel.SearchEvent.Success -> {
                        hiltViewModel.isLoading = View.GONE
                        recyclerview.adapter = MovieAdapter(it.response.movies)
                        waitingAnimation.turnOffAnimation()
                        hiltViewModel.isClickable = true
                    }
                }
                binding.isClickable = hiltViewModel.isClickable
                binding.isLoading = hiltViewModel.isLoading
            }
        }
        search_button.setOnClickListener {
            if (input_text.text.toString() == EMPTY_STRING) {
                Toast.makeText(it.context, EMPTY_SEARCH_WARNING, Toast.LENGTH_SHORT).show()
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
            hiltViewModel.searchMovies(hiltViewModel.searchSetup)
            disablingUI()

        }
    }

    //выключает интерфейс
    private fun disablingUI() {
        search_button.isClickable = false
        cardViewLastPageNavigation.isClickable = false
        cardViewBackPageNavigation.isClickable = false
        cardViewNextPageNavigation.isClickable = false
        cardViewFirstPageNavigation.isClickable = false
        waitingAnimation.turnOnAnimation()

    }

    //выпадающий список фильтра типа
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

/*
    //Запрашивает данные с сервера
    private fun requestData() {
        Log.e("!@#", "requestData was called")
        viewModel.getData(
            input_text.text.toString(),
            editTextYear.text.toString(),
            search_type_spinner.selectedItem.toString(),
            viewModel.currentPage
        )
    }



    //обновляем интерфейс
    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        Log.e("!@#", "Update UI!")
        waitingAnimation.turnOffAnimation()
        search_button.isClickable = true
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
        Log.e("!@#", "updateUI was called!")

        viewModel.movieData.observe(viewLifecycleOwner, {
                it?.let {
                    recyclerview.adapter = MovieAdapter(it)
                    waitingAnimation.turnOffAnimation()
                    search_button.isClickable = true
                }
            })
    }

    //вывод постраничной навигации вызывается только из UpdateUI()
    @SuppressLint("SetTextI18n")
    private fun bottomNavigation(currentPage: Int, pages: Int) {
        if (pages >= 2) {
            textViewPageNavigation.text = "$currentPage/$pages pages"
            cardViewBottomNavigation.visibility = CardView.VISIBLE
            if (currentPage > 1) {
                pageFirstPageNavigation.setTextColor(Color.BLACK)
                pageBackNavigation.setTextColor(Color.BLACK)
                cardViewFirstPageNavigation.setOnClickListener {
                    viewModel.currentPage = 1
                    requestData()
                    disablingUI()
                }
                cardViewBackPageNavigation.setOnClickListener {
                    viewModel.currentPage--
                    requestData()
                    disablingUI()
                }
            } else {
                pageFirstPageNavigation.setTextColor(Color.GRAY)
                pageBackNavigation.setTextColor(Color.GRAY)
                cardViewBackPageNavigation.isClickable = false
                cardViewFirstPageNavigation.isClickable = false
            }
            if (currentPage < pages) {
                pageNextPageNavigation.setTextColor(Color.BLACK)
                pageLastPageNavigation.setTextColor(Color.BLACK)
                cardViewNextPageNavigation.setOnClickListener {
                    viewModel.currentPage++
                    requestData()
                    disablingUI()
                }
                cardViewLastPageNavigation.setOnClickListener {
                    viewModel.currentPage = pages
                    requestData()
                    disablingUI()
                }
            } else {
                pageNextPageNavigation.setTextColor(Color.GRAY)
                pageLastPageNavigation.setTextColor(Color.GRAY)
                cardViewLastPageNavigation.isClickable = false
                cardViewNextPageNavigation.isClickable = false
            }
        } else cardViewBottomNavigation.visibility = CardView.GONE


    }
}*/