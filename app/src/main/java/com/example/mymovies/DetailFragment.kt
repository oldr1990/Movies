package com.example.mymovies

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import com.example.mymovies.databinding.DetailFragmentBinding
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.coroutines.*

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
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val request = GlobalScope.launch {
            viewModel.getData(arguments?.getString("title").toString())
        }
        runBlocking {
            request.join()
        }
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
        GlobalScope.launch {
            poster.load(viewModel.movieDetails.imgURL) {
                error(R.drawable.sorry_no_image_availble)
                scale(Scale.FILL)
                transformations(object : Transformation {
                    override fun key(): String = "paletteTransformer"
                    override suspend fun transform(
                        pool: BitmapPool,
                        input: Bitmap,
                        size: Size
                    ): Bitmap {
                        val p = Palette.from(input).generate()
                        if (p.vibrantSwatch != null) viewModel.movieDetails.colorBackground =
                            p.vibrantSwatch!!.rgb
                        if (p.darkVibrantSwatch != null) viewModel.movieDetails.colorText =
                            p.darkVibrantSwatch!!.rgb
                        binding.movie = viewModel.movieDetails
                        return input
                    }
                })
            }
        }
    }
}