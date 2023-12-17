package com.example.quetzalli.ui.views

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentUbicacionTestBinding
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class UbicacionTest : Fragment() {

    private lateinit var binding: FragmentUbicacionTestBinding
    private lateinit var navController: NavController
    private var scoreTotal = 0
    private var startTime: Long = 0
    private val userVm: UserVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUbicacionTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()

        //Inicia el cronometro
        startTime = SystemClock.elapsedRealtime()
    }

    private fun init() {
        navController = findNavController()
        binding.etUbicacion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.tilUbicacion.error = "Campo obligatorio"
                } else {
                    binding.tilUbicacion.error = null // Quita el mensaje de error
                }
            }
        })

    }


    private fun registerEvents() {

        binding.btnEnviar.setOnClickListener {
            val dateString = binding.etUbicacion.text.toString()
            if (dateString.isEmpty()) {
                binding.tilUbicacion.error = "Campo obligatorio"
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
            val currentDate = sdf.format(Date())
            scoreTotal = if (dateString == currentDate) {
                100
            } else {
                0
            }

            // Calcula el tiempo total en milisegundos
            val totalTimeMillis = SystemClock.elapsedRealtime() - startTime
            // Convierte a segundos totales
            val totalTimeSec = totalTimeMillis / 1000
            // Calcula los minutos y segundos
            val minutes = totalTimeSec / 60
            val seconds = totalTimeSec % 60
            // Formatea el tiempo en el formato MM:SS
            val totalTimeStr = String.format("%02d:%02d", minutes, seconds)
            val bundle = Bundle().apply {
                putString("userId", userVm.getCurrentUser()?.uid)
                putInt("scoreTotal", scoreTotal)
                putString("totalTime", totalTimeStr)
                putString("testType", "testubicacion")
            }
            navController.navigate(R.id.action_ubicacionTest_to_load, bundle)
        }
    }

}
