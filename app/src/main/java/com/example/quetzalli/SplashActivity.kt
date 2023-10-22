package com.example.quetzalli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quetzalli.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        binding.tvSlogan.setText(R.string.slogan)
    }

}