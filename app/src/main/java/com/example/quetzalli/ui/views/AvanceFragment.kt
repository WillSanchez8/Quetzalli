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
import com.example.quetzalli.databinding.FragmentAvanceBinding
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvanceFragment : Fragment() {
    private lateinit var binding: FragmentAvanceBinding
    private lateinit var navController: NavController
    private val userVM: UserVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentAvanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init() {
        navController = findNavController()

        val currentUser = userVM.getCurrentUser()
        val photoURL = currentUser?.photoUrl.toString()

        Glide.with(this).load(photoURL).into(binding.ivProfile)

    }

    private fun registerEvents() {

    }



}