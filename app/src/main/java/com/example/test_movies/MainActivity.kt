package com.example.test_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test_movies.ui.main.MainFragment
import com.example.test_movies.ui.main.ScrollingFragment
import kotlinx.android.synthetic.main.fragment_scrolling.*

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelected {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun itemTitle(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()

        descriptionTitleTextView.text = text

    }

}