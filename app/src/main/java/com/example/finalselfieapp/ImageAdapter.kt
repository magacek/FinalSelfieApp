package com.example.finalselfieapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * ImageAdapter manages the binding of image URIs to the RecyclerView in GalleryFragment.
 * It handles the layout of individual image items and manages user interaction with each image,
 * such as clicking to view in full screen. This adapter is essential for dynamically displaying
 * images in the gallery.
 *
 * @see RecyclerView.Adapter for managing views in a list.
 * @see Glide for loading images efficiently.
 * @see Uri for representing image locations.
 * @see Intent for starting FullScreenImageActivity on image click.
 *
 * @author Matt Gacek
 */

class ImageAdapter(
    private val imageUris: List<Uri>,
    private val onImageClick: (Uri) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]
        Glide.with(holder.imageView.context).load(uri).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val intent = Intent(holder.imageView.context, FullScreenImageActivity::class.java)
            intent.putExtra("IMAGE_URL", uri.toString())
            holder.imageView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = imageUris.size
}
