package com.example.quetzalli.ui.views

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentCountdownBinding

class CountdownFragment : Fragment() {

    private lateinit var binding: FragmentCountdownBinding
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountdownBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        navController = findNavController()
        Glide.with(binding.root).asGif()
            .load(R.drawable.icons8_clock)
            .into(binding.imgTimer)

        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                if (secondsRemaining > 0) {
                    binding.tvCountdown.text = secondsRemaining.toString()
                } else {
                    binding.tvCountdown.text = getText(R.string.right_now)
                }
            }

            override fun onFinish() {
                navController.navigate(R.id.action_countdown_to_memoryTest)
            }
        }

        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}
