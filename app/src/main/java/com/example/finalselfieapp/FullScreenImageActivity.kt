package com.example.finalselfieapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class FullScreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val imageView = findViewById<ImageView>(R.id.fullScreenImageView)
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: return

        Glide.with(this).load(imageUrl).into(imageView)
    }
}
