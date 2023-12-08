package com.example.quetzalli.ui.views

import android.os.Bundle
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init(){
        navController = findNavController()
    }

    private fun registerEvents(){
        binding.btnStart.setOnClickListener {
            val completedTest = arguments?.getString("completedTest")
            val bundle = Bundle().apply {
                putString("completedTest", completedTest)
            }
            navController.navigate(R.id.action_sesion_to_countdown, bundle)
        }
    }


}