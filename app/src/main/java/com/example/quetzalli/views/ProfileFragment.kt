package com.example.quetzalli.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentProfileBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private val userVM : UserVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    private fun init(view: View) {
        navController = findNavController()

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
            val input = TextInputEditText(view.context).apply {
                hint = resources.getString(R.string.hint_comentarios)
                setPadding(20, 10, 20, 10)
            }
            val inputLayout = TextInputLayout(view.context).apply {
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                addView(input)
            }
            MaterialAlertDialogBuilder(view.context)
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

            val intent = Intent(view.context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.btnEliminar.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle(resources.getString(R.string.delete_account))
                .setMessage(resources.getString(R.string.delete_account_message))
                .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}