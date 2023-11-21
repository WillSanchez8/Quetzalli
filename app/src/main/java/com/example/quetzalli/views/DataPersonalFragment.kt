package com.example.quetzalli.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentDataPersonalBinding

class DataPersonalFragment : Fragment() {

    private lateinit var binding: FragmentDataPersonalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentDataPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init() {

    }

    private fun registerEvents() {

    }


}