package com.example.quetzalli.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentTermsOfConditionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsOfConditionsFragment : Fragment() {

    private lateinit var binding: FragmentTermsOfConditionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsOfConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init(){

    }

    private fun registerEvents(){

    }

}