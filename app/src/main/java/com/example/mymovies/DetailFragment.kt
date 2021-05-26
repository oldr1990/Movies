package com.example.mymovies

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import com.example.mymovies.animations.WaitingAnimation
import com.example.mymovies.databinding.DetailFragmentBinding
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.coroutines.*
import java.net.UnknownHostException

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.isFailed.observe(viewLifecycleOwner, {
            if (it == true) findNavController().popBackStack()
        })
        val request = GlobalScope.launch {
            viewModel.getData(arguments?.getString("title").toString())
        }
        val waitingAnimation = WaitingAnimation(imageViewWaitingDetails, cardViewWaitingAnimationDetails)
        waitingAnimation.turnOnAnimation()
		//ожидаем данных с сервера
		runBlocking {
            request.join()
        }
        waitingAnimation.turnOffAnimation()
        Log.e("!@#", "after connetcion checking")
        ratingRecycler?.layoutManager = LinearLayoutManager(requireContext())
        ratingRecycler.adapter = RatingAdapter(viewModel.movieDetails.ratings)
        Log.e("!@#", viewModel.movieDetails.ratings.toString())
        if (arguments?.getInt("colorBackground") != 0) {
            viewModel.movieDetails.colorBackground = arguments?.getInt("colorBackground")!!
        } else viewModel.movieDetails.colorBackground = Color.GRAY
        if (arguments?.getInt("colorText") != 0) {
            viewModel.movieDetails.colorText = arguments?.getInt("colorText")!!
        } else viewModel.movieDetails.colorText = Color.BLACK
        binding.movie = viewModel.movieDetails
        poster.load(viewModel.movieDetails.imgURL)
    }
}