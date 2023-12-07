package com.example.quetzalli.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentLoadBinding
import com.example.quetzalli.viewmodel.MemoryVM
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Load : Fragment() {
    private lateinit var binding: FragmentLoadBinding
    private lateinit var navController: NavController
    private val memoryVM: MemoryVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString("userId")
        val scoreTotal = arguments?.getInt("scoreTotal")
        val totalTime = arguments?.getString("totalTime")

        val isUploadSuccessful = memoryVM.addTestMemoryData(userId!!, scoreTotal!!, totalTime!!)
        isUploadSuccessful.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_load_to_sesion)
            } else {
                Snackbar.make(binding.root, "Error al subir los datos", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        init()
    }

    private fun init() {
        navController = findNavController()
        Glide.with(binding.root).asGif()
            .load(R.drawable.icons8_gear)
            .into(binding.configuration)
    }

}
