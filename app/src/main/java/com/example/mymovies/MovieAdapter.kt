package com.example.mymovies

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.Placeholder
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.drawToBitmap
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Scale
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.example.mymovies.data.MovieSwatch
import com.example.test_movies.da.Movie
import com.google.android.material.shape.CutCornerTreatment
import com.google.android.material.shape.RoundedCornerTreatment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.new_list.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.annotation.Target as Target1

class MovieAdapter(val data: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.new_list, parent, false)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.titleText.text = data[position].name
        holder.itemView.yearText.text = "Год: ${data[position].year}"
        holder.itemView.typeText.text = "Тип: ${data[position].type}"
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.animations)
        if (data[position].colorBackground != 0) {
            holder.itemView.cardViewListItem.setCardBackgroundColor(
                data[position].colorBackground
            )

        }
        if (data[position].colorText != 0) {
            holder.itemView.titleText.setTextColor(data[position].colorText)
            holder.itemView.typeText.setTextColor(data[position].colorText)
            holder.itemView.yearText.setTextColor(data[position].colorText)

        }
        val job = GlobalScope.launch {
            holder.itemView.imageView.load(data[position].imgURL) {
                error(R.drawable.sorry_no_image_availble)
                scale(Scale.FILL)
                crossfade(true)
                crossfade(500)
                transformations(object : Transformation {
                    override fun key(): String = "paletteTransformer"
                    override suspend fun transform(
                        pool: BitmapPool,
                        input: Bitmap,
                        size: Size
                    ): Bitmap {
                        val p = Palette.from(input).generate()
                        if (p.vibrantSwatch != null) {
                            data[position].colorBackground =
                                p.vibrantSwatch!!.rgb
                        }
                        if (p.darkVibrantSwatch != null) {
                            data[position].colorText =
                                p.darkVibrantSwatch!!.rgb
                        }

                        if (data[position].colorBackground != 0) holder.itemView.cardViewListItem.setCardBackgroundColor(
                            data[position].colorBackground
                        )
                        if (data[position].colorText != 0) {
                            holder.itemView.titleText.setTextColor(data[position].colorText)
                            holder.itemView.typeText.setTextColor(data[position].colorText)
                            holder.itemView.yearText.setTextColor(data[position].colorText)
                        }
                        return input
                    }
                })
            }
        }
        runBlocking { job.join() }


        holder.itemView.cardView.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    holder.itemView.imageView to "imageTransition"
                )
                val bundle = Bundle()
                bundle.putString("title", data[position].imbdID)
                bundle.putInt("colorBackground", data[position].colorBackground)
                bundle.putInt("colorText", data[position].colorText)
                it.findNavController().navigate(R.id.detailFragment2, bundle,null, extras)
            }


    }

    override fun getItemCount(): Int = data.size
}