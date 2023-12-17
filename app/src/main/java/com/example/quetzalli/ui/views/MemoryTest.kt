package com.example.quetzalli.ui.views

import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.quetzalli.R
import com.example.quetzalli.data.models.SequenceGraph
import com.example.quetzalli.databinding.FragmentMemoryTestBinding
import com.example.quetzalli.viewmodel.MemoryVM
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoryTest : Fragment() {

    private lateinit var binding: FragmentMemoryTestBinding
    private val memoryVM: MemoryVM by viewModels()
    private lateinit var navController: NavController
    private var scoreTotal = 0
    private var currentLevel = 0
    private val userVm: UserVM by viewModels()
    private var currentSequence: SequenceGraph? = null
    private var startTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoryTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
    }

    private fun init() {
        navController = findNavController()
        memoryVM.sequences.observe(viewLifecycleOwner) { sequences ->
            sequences?.let {
                currentSequence = sequences.random()
                currentSequence?.let { sequence ->
                    // Muestra el indicador de progreso antes de iniciar la carga de la imagen
                    binding.LoadingIndicator.visibility = View.VISIBLE

                    Glide.with(binding.root)
                        .load(sequence.levels?.get(0)?.imgSequence)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Oculta el indicador de progreso en caso de error
                                Snackbar.make(
                                    binding.root,
                                    "Error al obtener la secuencia",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Oculta el indicador de progreso en caso de exito
                                binding.LoadingIndicator.visibility = View.GONE

                                //Inicia el cronometro
                                startTime = SystemClock.elapsedRealtime()
                                return false
                            }
                        })
                        .into(binding.imgSequence)
                } ?: run {
                    Snackbar.make(
                        binding.root,
                        "Error al obtener la secuencia",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        memoryVM.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerEvents() {
        val buttons = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD, binding.btnE)
        buttons.forEach { button ->
            button.setOnClickListener { view ->
                val userAnswer = button.text.toString()
                val correctAnswer = currentSequence?.levels?.get(currentLevel)?.answer
                if (userAnswer == correctAnswer) {
                    scoreTotal += 50
                }
                currentLevel++
                if (currentLevel < (currentSequence?.levels?.size ?: 0)) {
                    Glide.with(binding.root)
                        .load(currentSequence?.levels?.get(currentLevel)?.imgSequence)
                        .into(binding.imgSequence)
                    if (currentLevel == 1) {
                        // Ilumina el progressIndicator al 50%
                        binding.progressIndicator.progress = 50
                    }
                } else {
                    // Ilumina el progressIndicator al 100%
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
                        putString("testType", "testmemory")
                    }
                    navController.navigate(R.id.action_memoryTest_to_load, bundle)
                }
            }
        }
    }
}

