package com.example.quetzalli.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentDataPersonalBinding
 // VIEW MODEL
import com.example.quetzalli.viewmodel.UserVM
import androidx.fragment.app.viewModels

class DataPersonalFragment : Fragment() {
    private val userVM: UserVM by viewModels()

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
        // Load user data into the form
        loadUserData()

    }

    private fun registerEvents() {

    }

    // Function to load user data into the form
    private fun loadUserData() {
        userVM.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.etName.setText(user.displayName)
            }
        }
    }


}