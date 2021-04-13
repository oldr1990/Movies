package com.example.test_movies.ui.main

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.test_movies.R
import com.example.test_movies.da.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.new_list.view.*
import kotlin.coroutines.coroutineContext

class MovieAdapter(val data: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.new_list, parent, false)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.titleText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity
                val scrollingFragment = ScrollingFragment()
                val bundle =  Bundle()
                bundle.putParcelable("movie",data[position])
                scrollingFragment.arguments = bundle
                scrollingFragment.textInputEditText
                activity.supportFragmentManager.beginTransaction().replace(R.id.main,scrollingFragment).addToBackStack(null).commit()


            }
        })
        holder.item.titleText.text = data[position].name
        holder.item.yearText.text = "Год: ${data[position].year}"
        holder.item.typeText.text = "Тип: ${data[position].type}"
        Picasso.get().load(data[position].imgURL).resize(400, 600).centerCrop()
            .into(holder.item.imageView)

    }

    override fun getItemCount(): Int = data.size


}
