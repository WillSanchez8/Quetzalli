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
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
            if (isLoggedIn) {
                // Si el usuario ya ha iniciado sesión, diríge a la actividad principal
                startActivity(Intent(this@IniActivity, MainActivity::class.java))
            } else {
                // Si el usuario no ha iniciado sesión, diríge a la actividad de inicio de sesión
                startActivity(Intent(this@IniActivity, LoginActivity::class.java))
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}