package com.example.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.mymovies.data.Constants.BACK_PRESSED_MESSAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var backPressedTimer = 0L

    override fun onBackPressed() {
        if(System.currentTimeMillis() - backPressedTimer < 2000)
            exitProcess(-1)
        else {
            backPressedTimer = System.currentTimeMillis()
            Toast.makeText(this, BACK_PRESSED_MESSAGE,Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyMovies)
        setContentView(R.layout.activity_main)
    }
}