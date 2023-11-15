package com.example.finalselfieapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
/**
 * FullScreenImageActivity displays a full-screen image when a user selects an image
 * from the gallery. It loads the image URL passed from the intent and uses Glide
 * to display it. This activity enhances the user experience by providing a detailed view
 * of the selected image.
 *
 * @see AppCompatActivity for the basic application support and context.
 * @see Glide for efficient image loading and rendering.
 * @see ImageView for displaying the full-screen image.
 *
 * @author Matt Gacek
 */

class FullScreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val imageView = findViewById<ImageView>(R.id.fullScreenImageView)
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: return

        Glide.with(this).load(imageUrl).into(imageView)
    }
}
