package com.example.finalselfieapp
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.finalselfieapp.GalleryFragment
import com.example.finalselfieapp.MainActivity
import com.example.finalselfieapp.databinding.FragmentCameraBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after onDestroyView")

    private var imageCapture: ImageCapture? = null

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }

        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(context, "Failed to start camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                uploadPhoto(Uri.fromFile(photoFile))
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Image capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadPhoto(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${uri.lastPathSegment}")
        storageRef.putFile(uri).addOnSuccessListener {
            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).replaceFragment(GalleryFragment())
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).replaceFragment(GalleryFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Correctly clear the binding
        imageCapture = null // Release the camera resources
    }
}
