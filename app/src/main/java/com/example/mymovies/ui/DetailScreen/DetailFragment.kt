package com.example.mymovies.ui.DetailScreen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import coil.load
import com.example.mymovies.R
import com.example.mymovies.ui.adapters.RatingAdapter
import com.example.mymovies.databinding.DetailFragmentBinding
import com.example.mymovies.ui.MainScreen.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }



    lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.detail_fragment, container, false
        )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel: DetailViewModel by viewModels()
        ratingRecycler?.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getData(arguments?.getString("title").toString())
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.detailResponse.collect {
                when (it) {
                    DetailViewModel.DetailsEvent.Empty -> {
                     waitingConstraint.visibility = View.GONE
                        Log.e("!@#", "DetailViewModel: Empty...")
                    }
                    is DetailViewModel.DetailsEvent.Failure -> {
                        waitingConstraint.visibility = View.GONE
                        view?.findNavController()?.popBackStack()
                        Log.e("!@#", "DetailViewModel: Failure...")
                    }
                    DetailViewModel.DetailsEvent.Loading -> {
                        waitingConstraint.visibility = View.VISIBLE
                        Log.e("!@#", "DetailViewModel: Loading...")
                    }
                    is DetailViewModel.DetailsEvent.Success -> {
                        waitingConstraint.visibility = View.GONE
                        Log.e("!@#", "DetailViewModel: Success...")
                        Log.e("!@#", "DetailViewModel: ${it.response}")
                        ratingRecycler.adapter = RatingAdapter(it.response.ratings)
                        if (arguments?.getInt("colorBackground") != 0) {
                            it.response.colorBackground = arguments?.getInt("colorBackground")!!
                        } else it.response.colorBackground = Color.GRAY
                        if (arguments?.getInt("colorText") != 0) {
                            it.response.colorText = arguments?.getInt("colorText")!!
                        } else it.response.colorText = Color.BLACK
                        poster.load(it.response.imgURL)
                        binding.movie = it.response

                    }
                }
            }
        }

    }
}