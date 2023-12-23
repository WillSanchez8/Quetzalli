package com.example.quetzalli.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentGenerationBinding
import com.example.quetzalli.viewmodel.UserVM

class Generation : Fragment() {

    private lateinit var binding: FragmentGenerationBinding
    private val userVM: UserVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentGenerationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init() {
        val userId= userVM.getCurrentUser()


    }

    private fun registerEvents() {

    }

}