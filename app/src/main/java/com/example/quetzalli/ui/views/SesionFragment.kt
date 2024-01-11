package com.example.quetzalli.ui.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentSesionBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class SesionFragment : Fragment() {

    private lateinit var binding: FragmentSesionBinding
    private lateinit var navController: NavController
    private var completedTest: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
        completedTest = arguments?.getString("completedTest")
    }

    private fun init() {
        navController = findNavController()

    }

    private fun registerEvents() {
        binding.btnStart.setOnClickListener {
            // Obtén la fecha actual
            val currentDate = LocalDate.now()

            // Obtén la fecha y el contador guardados en las preferencias compartidas
            val sharedPreferences = this.requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
            val savedDate = sharedPreferences.getString("date", "")
            val testCount = sharedPreferences.getInt("testCount", 0)

            // Compara las fechas
            if (savedDate != currentDate.toString()) {
                // Si las fechas son diferentes, permite que el usuario realice las pruebas y guarda la fecha actual
                sharedPreferences.edit().putString("date", currentDate.toString()).apply()
                sharedPreferences.edit().putInt("testCount", 0).apply() // Restablece el contador de pruebas

                val bundle = Bundle().apply {
                    putString("completedTest", completedTest)
                }
                navController.navigate(R.id.action_sesion_to_countdown, bundle)
            } else if (testCount < 3) {
                // Si las fechas son iguales pero el usuario ha realizado menos de 3 pruebas, permite que el usuario realice la prueba
                sharedPreferences.edit().putInt("testCount", testCount + 1).apply() // Incrementa el contador de pruebas

                val bundle = Bundle().apply {
                    putString("completedTest", completedTest)
                }
                navController.navigate(R.id.action_sesion_to_countdown, bundle)
            } else {
                // Si las fechas son iguales y el usuario ha realizado 3 pruebas, muestra un mensaje al usuario
                Snackbar.make(
                    binding.root,
                    "Has completado las pruebas por hoy",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

}
