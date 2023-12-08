package com.example.quetzalli.ui.views

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.data.models.Operations
import com.example.quetzalli.databinding.FragmentCalculoTestBinding
import com.example.quetzalli.viewmodel.CalculationVM
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class CalculoTest : Fragment() {

    private lateinit var binding: FragmentCalculoTestBinding
    private lateinit var navController: NavController
    private val calculationVM: CalculationVM by viewModels()
    private var scoreTotal = 0
    private var startTime: Long = 0
    private val userVm: UserVM by viewModels()
    private var operations = listOf<Operations>()
    private var selectedOperations = listOf<Operations>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCalculoTestBinding.inflate(inflater, container, false)
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
        calculationVM.operations.observe(viewLifecycleOwner) { ops ->
            operations = ops
            val randomIndices = List(4) { Random.nextInt(operations.size) }
            selectedOperations = randomIndices.map { operations[it] }
            // Carga la primera imagen
            Glide.with(binding.root)
                .load(selectedOperations[currentOperationIndex].operations?.get(0)?.img)
                .into(binding.ivOperacion)
        }
    }

    private var currentOperationIndex = 0

    private fun registerEvents() {
        binding.btnSiguiente.setOnClickListener {
            val userAnswer = binding.editRespuesta.text.toString()
            if (userAnswer.isEmpty()) {
                // Muestra un mensaje de error si el campo de texto está vacío
                binding.editRespuesta.error = "Debes ingresar una respuesta"
            } else {
                // Comprueba si la respuesta del usuario es correcta
                val correctAnswer = selectedOperations[currentOperationIndex].operations?.get(0)?.answer
                if (correctAnswer != null && userAnswer.toInt() == correctAnswer) {
                    scoreTotal += 25
                }
            }
            // Pasa a la siguiente operación
            currentOperationIndex++
            if (currentOperationIndex < selectedOperations.size) {
                Glide.with(binding.root)
                    .load(selectedOperations[currentOperationIndex].operations?.get(0)?.img)
                    .into(binding.ivOperacion)
                // Actualiza el LinearProgressIndicator
                binding.progressIndicator.progress = (currentOperationIndex + 1) * 25
                // Cambia el texto del botón a "Enviar" en la última operación
                if (currentOperationIndex == selectedOperations.size - 1) {
                    binding.btnSiguiente.text = "Enviar"
                }
            } else {
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
                    putString("testType", "testcalculation")
                }
                navController.navigate(R.id.action_calculoTest_to_load, bundle)
            }
        }
    }


}