package com.example.mymovies.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovies.R
import com.example.mymovies.models.Ratings
import kotlinx.android.synthetic.main.rating_list_item_imbd.view.*
import kotlinx.android.synthetic.main.rating_list_item_metacritic.view.*
import kotlinx.android.synthetic.main.rating_list_item_rootten.view.*
import kotlinx.android.synthetic.main.rating_list_item_rootten.view.imageRatingItemRT

//Адаптер для списка рейтингов, для каждого типа рейтинга свой лейаут
class RatingAdapter(private val ratings: List<Ratings>) :
    RecyclerView.Adapter<RatingAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    enum class Rating {
        METACRITIC, ROTTEN_TOMATOES, IMD
    }

    //устанавливаем тип в зависимости от источника
    override fun getItemViewType(position: Int): Int {
        when (ratings[position].source) {
            "Metacritic" -> return Rating.METACRITIC.ordinal
            "Rotten Tomatoes" -> return Rating.ROTTEN_TOMATOES.ordinal
            "Internet Movie Database" -> return Rating.IMD.ordinal

        }
        return Rating.METACRITIC.ordinal
    }

    //возвращаем нужный лейаут в зависимости от типа
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            Rating.METACRITIC.ordinal -> return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rating_list_item_metacritic, parent, false)
            )
            Rating.ROTTEN_TOMATOES.ordinal -> return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rating_list_item_rootten, parent, false)
            )
            Rating.IMD.ordinal -> return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rating_list_item_imbd, parent, false)
            )
        }
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rating_list_item_rootten, parent, false)
        return ViewHolder(textView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (ratings[position].source) {
            "Metacritic" -> {
                holder.itemView.imageMetacritic.load(R.drawable.metacritic)
                val numbers = ratings[position].value.split("/")
                holder.itemView.value_metacritic.text = numbers[0]
                when (numbers[0].toInt()) {
                    in (0..39) -> holder.itemView.cardMetacriticValue.setCardBackgroundColor(Color.RED)
                    in (40..59) -> holder.itemView.cardMetacriticValue.setCardBackgroundColor(Color.YELLOW)
                    in (60..100) -> holder.itemView.cardMetacriticValue.setCardBackgroundColor(Color.GREEN)
                }
            }
            "Rotten Tomatoes" -> {
                holder.itemView.value_rt.text = ratings[position].value
                val numbers = ratings[position].value.split("%")
                when (numbers[0].toInt()) {
                    in (0..59) -> {
                        holder.itemView.imageRatingItemRT.load(R.drawable.rt_rotten)
                        holder.itemView.value_rt.setTextColor(Color.RED)
                    }
                    in (60..74) -> {
                        holder.itemView.imageRatingItemRT.load(R.drawable.rt_avarage)
                        holder.itemView.value_rt.setTextColor(Color.YELLOW)
                    }
                    in (74..100) -> {
                        holder.itemView.imageRatingItemRT.load(R.drawable.rt_best)
                        holder.itemView.value_rt.setTextColor(Color.GREEN)
                    }
                }
            }
            "Internet Movie Database" -> {
                holder.itemView.imageIMDbStar.load(R.drawable.ic_baseline_star_rate_24)
                holder.itemView.value.text = ratings[position].value
            }
        }
    }

    override fun getItemCount(): Int = ratings.size
}
