package com.example.quetzalli.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentSesionBinding

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

        // Cambia el texto de los TextViews en función del test completado
        when (completedTest) {
            "testubicacion" -> {
                binding.tvSession.text = getString(R.string.test_location)
                binding.tvInstructions.text = getString(R.string.test_location_instructions)
                binding.tvDuration.text =
                    getString(R.string.duration, getString(R.string.test_location_duration))
                binding.tvDuration.visibility = View.VISIBLE
            }

            "testmemory" -> {
                binding.tvSession.text = getString(R.string.test_mem)
                binding.tvInstructions.text = getString(R.string.test_mem_instructions)
                binding.tvDuration.text =
                    getString(R.string.duration, getString(R.string.test_mem_duration))
                binding.tvDuration.visibility = View.VISIBLE
            }

            "testcalculation" -> {
                binding.tvSession.text = getString(R.string.test_calc)
                binding.tvInstructions.text = getString(R.string.test_calc_instructions)
                binding.tvDuration.text =
                    getString(R.string.duration, getString(R.string.test_calc_duration))
                binding.tvDuration.visibility = View.VISIBLE
            }

            else -> {
                binding.tvDuration.visibility = View.GONE
            }
        }
    }

    private fun registerEvents() {
        binding.btnStart.setOnClickListener {
            val bundle = Bundle().apply {
                putString("completedTest", completedTest)
            }
            navController.navigate(R.id.action_sesion_to_countdown, bundle)
        }
    }

}