package com.example.quetzalli.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 204
    }

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private val userVM: UserVM by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
    }

    private fun init() {
        navController = findNavController()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun registerEvents() {
        binding.btnSignInWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                userVM.signInWithGoogle(account.idToken!!)
                userVM.signInResult.observe(viewLifecycleOwner) { result ->
                    if (result is FetchResult.Success) {
                        val firebaseUser = result.data
                        userVM.getUserById(firebaseUser.uid)
                            .observe(viewLifecycleOwner) { userResult ->
                                if (userResult is FetchResult.Success && userResult.data != null) {
                                    // El usuario ya existe, navega a MainActivity
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                } else if (userResult is FetchResult.Success && userResult.data == null) {
                                    // El usuario no existe, navega a RegisterFragment
                                    navController.navigate(R.id.action_signIn_to_register)
                                } else if (userResult is FetchResult.Error) {
                                    // Error al obtener el usuario, muestra un mensaje de error
                                    Snackbar.make(
                                        binding.root,
                                        "Error al obtener el usuario, intente de nuevo más tarde",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else if (result is FetchResult.Error) {
                        // Sign in fail
                        val exception = result.exception
                        Snackbar.make(
                            binding.root,
                            "Error al iniciar sesión, intente de nuevo más tarde",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.e("No CONNECTION", "$e")
                Snackbar.make(
                    binding.root,
                    "Error al iniciar sesión, intente de nuevo más tarde",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

}



