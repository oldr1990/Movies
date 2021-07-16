package com.example.mymovies.ui.adapters

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import com.example.mymovies.R
import com.example.mymovies.models.Movie
import kotlinx.android.synthetic.main.new_list.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PagingAdapter : PagingDataAdapter<Movie, PagingAdapter.ViewHolder>(MovieDefferentiator) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: Movie("")
        holder.itemView.titleText.text = item.name
        holder.itemView.yearText.text = "Год: ${item.year}"
        holder.itemView.typeText.text = "Тип: ${item.type}"
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.animations
        )
        if (item.colorBackground != 0) {
            holder.itemView.cardViewListItem.setCardBackgroundColor(
                item.colorBackground
            )
        }
        if (item.colorText != 0) {
            holder.itemView.titleText.setTextColor(item.colorText)
            holder.itemView.typeText.setTextColor(item.colorText)
            holder.itemView.yearText.setTextColor(item.colorText)
        }

        //загружаем постер и определяем цвета
        val job = GlobalScope.launch {
            holder.itemView.imageView.load(item.imgURL) {
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
                            item.colorBackground =
                                p.vibrantSwatch!!.rgb
                        }
                        if (item.colorBackground != 0) holder.itemView.cardViewListItem.setCardBackgroundColor(
                            item.colorBackground
                        )
                        return input
                    }
                })
            }
        }
        //ждем загрузки постера и определения цвета
        runBlocking { job.join() }
        holder.itemView.cardView.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                holder.itemView.imageView to "imageTransition"
            )
            val bundle = Bundle()
            bundle.putString("title", item.imbdID)
            bundle.putInt("colorBackground", item.colorBackground)
            bundle.putInt("colorText", item.colorText)
            it.findNavController().navigate(R.id.detailFragment2, bundle, null, extras)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.new_list, parent, false)
        )
    }

    object MovieDefferentiator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imbdID == newItem.imbdID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

}