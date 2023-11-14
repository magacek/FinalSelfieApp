package com.example.finalselfieapp

import CameraFragment
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalselfieapp.databinding.FragmentGalleryBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        binding.openCameraButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(CameraFragment())
        }
        fetchImages()
    }

    private fun uploadPhoto(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${uri.lastPathSegment}")
        storageRef.putFile(uri).addOnSuccessListener {
            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            // Refresh the gallery
            if (activity is MainActivity) {
                (activity as MainActivity).replaceFragment(GalleryFragment().apply { fetchImages() })
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchImages() {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/")
        storageRef.listAll().addOnSuccessListener { listResult ->
            val imageUrls = listResult.items.map { it.downloadUrl }
            Tasks.whenAllSuccess<Uri>(imageUrls).addOnSuccessListener { uris ->
                val adapter = ImageAdapter(uris) { uri ->
                    // Handle image click, open in full screen
                }
                binding.recyclerView.adapter = adapter
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch images", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
