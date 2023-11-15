package com.example.finalselfieapp


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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
/**
 * GalleryFragment manages the display of user's uploaded photos in a grid layout.
 * It handles fetching images from Firebase Storage, displaying them using RecyclerView,
 * and provides functionalities like sign-out and navigating to the camera. It is a central
 * component for managing and displaying the photo gallery.
 *
 * @see Fragment for fragment lifecycle and user interface.
 * @see RecyclerView for displaying images in a grid layout.
 * @see FirebaseStorage for fetching images from Firebase.
 * @see FirebaseAuth for managing user authentication.
 * @see GridLayoutManager for arranging images in a grid.
 *
 * @author Matt Gacek
 */

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
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columns

        binding.signOutButton.setOnClickListener {
            // Clear the gallery before signing out
            binding.recyclerView.adapter = null
            FirebaseAuth.getInstance().signOut()
            navigateToLoginScreen()
        }
        binding.openCameraButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(CameraFragment())
        }
        fetchImages()
    }

    private fun navigateToLoginScreen() {
        if (activity is MainActivity) {
            (activity as MainActivity).replaceFragment(LoginRegisterFragment())
        }    }


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
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$userId")
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
        } else {
            Toast.makeText(context, "Failed to fetch images", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}