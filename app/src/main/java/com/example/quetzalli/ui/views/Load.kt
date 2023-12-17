package com.example.quetzalli.ui.views

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
import com.example.quetzalli.databinding.FragmentLoadBinding
import com.example.quetzalli.viewmodel.TestVM
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Load : Fragment() {
    private lateinit var binding: FragmentLoadBinding
    private lateinit var navController: NavController
    private val testVM : TestVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testType = arguments?.getString("testType")
        val userId = arguments?.getString("userId")
        val scoreTotal = arguments?.getInt("scoreTotal")
        val totalTime = arguments?.getString("totalTime")

        val isUploadSuccessful = when (testType) {
            "testubicacion" -> testVM.addTestData("testspace", userId!!, scoreTotal!!, totalTime!!)
            "testmemory" -> testVM.addTestData("testmemory", userId!!, scoreTotal!!, totalTime!!)
            "testcalculation" -> testVM.addTestData("testcalculation", userId!!, scoreTotal!!, totalTime!!)
            else -> throw IllegalArgumentException("Tipo de prueba desconocido: $testType")
        }

        isUploadSuccessful.observe(viewLifecycleOwner) { success ->
            if (success) {
                val bundle = Bundle().apply {
                    putString("completedTest", testType)
                }
                // Navega de nuevo al fragmento SesionFragment si la subida de datos fue exitosa
                navController.navigate(R.id.action_load_to_sesion, bundle)
            } else {
                // Muestra un mensaje de error si la subida de datos falla
                Snackbar.make(binding.root, "Error al subir los datos", Snackbar.LENGTH_SHORT).show()
            }
        }

        init()
    }

    private fun init() {
        navController = findNavController()
        Glide.with(binding.root).asGif()
            .load(R.drawable.icons8_gear)
            .into(binding.configuration)
    }

}
