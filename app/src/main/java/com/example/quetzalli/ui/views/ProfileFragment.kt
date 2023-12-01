package com.example.quetzalli.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.databinding.FragmentProfileBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private val userVM: UserVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init() {
        navController = findNavController()

        val currentUser = userVM.getCurrentUser()
        val photoURL = currentUser?.photoUrl.toString()

        Glide.with(this).load(photoURL).into(binding.ivProfile)

    }

    private fun registerEvents(){
        binding.btnCambio.setOnClickListener {
            navController.navigate(R.id.action_perfil_to_dataPersonalFragment)
        }

        binding.btnPoliticas.setOnClickListener {
            navController.navigate(R.id.action_perfil_to_policyFragment)
        }

        binding.btnAyuda.setOnClickListener {
            navController.navigate(R.id.action_perfil_to_helpFragment)
        }

        binding.btnComentarios.setOnClickListener {
            val input = TextInputEditText(requireContext()).apply {
                hint = resources.getString(R.string.hint_comentarios)
                setPadding(20, 10, 20, 10)
            }
            val inputLayout = TextInputLayout(requireContext()).apply {
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                addView(input)
            }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.comentarios))
                .setMessage(resources.getString(R.string.comentarios_message))
                .setView(inputLayout)
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    val comentario = input.text.toString()
                    // Aquí puedes manejar el comentario del usuario
                    dialog.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.btnCerrarSesion.setOnClickListener {
            userVM.logout()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.btnEliminar.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.delete_account))
                .setMessage(resources.getString(R.string.delete_account_message))
                .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                    userVM.deleteUser().observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is FetchResult.Success -> {
                                // La cuenta del usuario se ha eliminado exitosamente
                                // Ahora puedes redirigir al usuario a la pantalla de inicio de sesión
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                                requireActivity().finishAffinity()
                            }

                            is FetchResult.Error -> {
                                // Hubo un error al eliminar la cuenta del usuario
                                // Muestra un Snackbar con un mensaje de error
                                Snackbar.make(
                                    binding.root,
                                    "Ha ocurrido un error mientras se eliminaba tu cuenta, intenta de nuevo más tarde",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}