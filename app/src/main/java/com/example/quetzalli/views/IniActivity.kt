package com.example.quetzalli.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quetzalli.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IniActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(1200L)
            val intent = if (getUserChoice()) {
                // El usuario ya ha iniciado sesión, navega a la pantalla principal
                Intent(this@IniActivity, MainActivity::class.java)
            } else {
                // El usuario no ha iniciado sesión, navega a la pantalla de inicio de sesión
                Intent(this@IniActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun getUserChoice(): Boolean {
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPref.getBoolean("UserChoice", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}