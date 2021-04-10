package com.example.test_movies.ui.main

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_movies.R
import com.example.test_movies.da.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.new_list.view.*

class MovieAdapter(val data: List<Movie>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(val item: View): RecyclerView.ViewHolder(item){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.new_list,parent,false)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.item.titleText.text = data[position].name
        holder.item.yearText.text = "Год: ${data[position].year}"
        holder.item.typeText.text = "Тип: ${data[position].type}"
        Picasso.get().load(data[position].imgURL).resize(400,600).centerCrop().into(holder.item.imageView)

    }

    override fun getItemCount(): Int = data.size
    }
