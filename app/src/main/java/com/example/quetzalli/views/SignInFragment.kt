package com.example.quetzalli.views

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.databinding.FragmentSignInBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private val userVM : UserVM by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    // Define el lanzador de resultados de actividad
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            handleSignInResult(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleSignInClient()
        init()
        registerEvents()
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun init(){
        navController = findNavController()
    }

    private fun registerEvents(){
        binding.btnSignInWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

        userVM.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // Si el inicio de sesión fue exitoso, verifica si el perfil del usuario está completo
                val sharedPref = activity?.getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val isProfileComplete = sharedPref?.getBoolean("isProfileComplete", false)
                if (isProfileComplete == true) {
                    // Si el perfil del usuario está completo, dirígelo a MainActivity
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // Si el perfil del usuario no está completo, dirígelo a RegisterFragment
                    findNavController().navigate(R.id.action_signIn_to_register)
                }
            } else {
                // Muestra un mensaje de error
                Snackbar.make(binding.root, "Ocurrió un error", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            userVM.loginWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Maneja la excepción
        }
    }
}

