package com.example.finalselfieapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalselfieapp.databinding.FragmentLoginRegisterBinding
import com.google.firebase.auth.FirebaseAuth
/**
 * LoginRegisterFragment provides the user interface and logic for user authentication.
 * It handles both login and registration functionalities using Firebase Authentication.
 * This fragment is the entry point for users to access the application's features.
 *
 * @see Fragment for fragment lifecycle and user interface.
 * @see FirebaseAuth for handling user authentication with Firebase.
 * @see MainActivity for navigating to other fragments upon successful login or registration.
 *
 * @author Matt Gacek
 */

class LoginRegisterFragment : Fragment() {
    private var _binding: FragmentLoginRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            (activity as MainActivity).replaceFragment(GalleryFragment())
                        } else {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            (activity as MainActivity).replaceFragment(GalleryFragment())
                        } else {
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
