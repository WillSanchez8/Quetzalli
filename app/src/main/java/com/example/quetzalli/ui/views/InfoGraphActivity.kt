package com.example.quetzalli.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.databinding.ActivityInfoGraphBinding
import com.example.quetzalli.network.response.ApiResponse
import com.example.quetzalli.viewmodel.TestVM
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class InfoGraphActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoGraphBinding
    private val userVM : UserVM by viewModels()
    private val testVM : TestVM by viewModels()
    private var currentGraphUrl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        registerEvents()
        testVM.error.observe(this) { errorMessage ->
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun init() {
        val testType = intent.getStringExtra("testType")
        val userId = userVM.getCurrentUser()?.uid

        // Mostrar el indicador de progreso
        binding.loading.visibility = View.VISIBLE

        when(testType){
            "testmemory" -> {
                binding.tvTitleTest.text = "Test de Memoria"
                if (userId != null) {
                    testVM.createGraph1(userId).observe(this) { response ->
                        // Ocultar el indicador de progreso
                        binding.loading.visibility = View.GONE
                        handleApiResponse(response)
                    }
                }
            }
            "testcalculation" -> {
                binding.tvTitleTest.text = "Test de Atención y Cálculo"
                if (userId != null) {
                    testVM.createGraph2(userId).observe(this) { response ->
                        // Ocultar el indicador de progreso
                        binding.loading.visibility = View.GONE
                        handleApiResponse(response)
                    }
                }
            }
            "testspace" -> {
                binding.tvTitleTest.text = "Test Espacio-Temporal"
                if (userId != null) {
                    testVM.createGraph3(userId).observe(this) { response ->
                        // Ocultar el indicador de progreso
                        binding.loading.visibility = View.GONE
                        handleApiResponse(response)
                    }
                }
            }
        }
    }

    private fun handleApiResponse(response: ApiResponse) {
        if (response.url != null) {
            currentGraphUrl = response.url
            Glide.with(this).load(response.url).into(binding.ivGraph)
        } else if (response.message != null) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Atención")
                .setMessage(response.message)
                .setPositiveButton("Aceptar") { dialog, _ ->
                    onBackPressed()
                }
                .show()
        }
    }

    private fun registerEvents() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
