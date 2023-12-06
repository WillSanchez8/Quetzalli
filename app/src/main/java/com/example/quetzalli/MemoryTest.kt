package com.example.quetzalli

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.quetzalli.databinding.FragmentMemoryTestBinding
import com.example.quetzalli.viewmodel.MemoryVM
import com.google.android.material.snackbar.Snackbar

class MemoryTest : Fragment() {

    private lateinit var binding: FragmentMemoryTestBinding
    private val memoryVM: MemoryVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoryTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
    }

    private fun init(){
        memoryVM.sequences.observe(viewLifecycleOwner) { sequences ->
            sequences?.let {
                Glide.with(binding.root)
                    .load(it[0].levels?.get(0)?.imgSequence)
                    .into(binding.imgSequence)
            }
        }

        memoryVM.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerEvents(){

    }


}