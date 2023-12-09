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
import com.example.quetzalli.data.models.Operation
import com.example.quetzalli.databinding.FragmentCalculoTestBinding
import com.example.quetzalli.viewmodel.CalculationVM
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculoTest : Fragment() {

    private lateinit var binding: FragmentCalculoTestBinding
    private lateinit var navController: NavController
    private val calculationVM: CalculationVM by viewModels()
    private var scoreTotal = 0
    private var startTime: Long = 0
    private val userVm: UserVM by viewModels()

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

    private var currentOperationIndex = 0
    private var operationsSubList: List<Operation>? = null

    private fun init() {
        navController = findNavController()
        calculationVM.operations.observe(viewLifecycleOwner) { operations ->
            operations?.let {
                operationsSubList = it.flatMap { it.operations ?: emptyList() }.shuffled().take(4)
                operationsSubList?.let { subList ->
                    Glide.with(binding.root)
                        .load(subList[currentOperationIndex].img)
                        .into(binding.ivOperacion)
                }
            }
        }
    }

    private fun registerEvents() {
        binding.btnSiguiente.setOnClickListener {
            val userAnswer = binding.editRespuesta.text.toString()
            if (userAnswer.isEmpty()) {
                binding.tilAnswer.error = "Campo obligatorio"
                return@setOnClickListener
            }
            val correctAnswer = operationsSubList?.get(currentOperationIndex)?.answer
            if (userAnswer.toInt() == correctAnswer) {
                scoreTotal += 25
            }
            currentOperationIndex++
            if (currentOperationIndex < (operationsSubList?.size ?: 0)) {
                Glide.with(binding.root)
                    .load(operationsSubList?.get(currentOperationIndex)?.img)
                    .into(binding.ivOperacion)
                binding.progressIndicator.progress += 25
            } else {
                binding.progressIndicator.progress = 100
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
            if (currentOperationIndex == (operationsSubList?.size ?: (0 - 1))) {
                binding.btnSiguiente.text = "Enviar"
            }
        }
    }
}