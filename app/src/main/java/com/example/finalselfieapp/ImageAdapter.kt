package com.example.finalselfieapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
