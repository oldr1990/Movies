package com.example.mymovies.ui.animations

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView

class WaitingAnimation(imageView: ImageView, cardView: CardView) {
   private var cardview = cardView
   private var animation = ObjectAnimator.ofFloat(imageView, View.ROTATION, -360f, 0f)
    fun turnOnAnimation() {
        cardview.visibility = CardView.VISIBLE
        animation.duration = 1000
        animation.repeatCount = ValueAnimator.INFINITE
        animation.start()
    }
     fun turnOffAnimation() {
    if (animation.isStarted) animation.end()
        cardview.visibility = CardView.GONE
    }
}