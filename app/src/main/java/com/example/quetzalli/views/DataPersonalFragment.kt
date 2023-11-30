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
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.models.User

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
        userVM.getUserById(userVM.getCurrentUser()?.uid?:"").observe(viewLifecycleOwner) { result ->
            when (result) {
                is FetchResult.Success -> {
                    // name
                    binding.etName.setText(result.data?.name).toString()
                    // occupation
                    binding.etOcupation.setText(result.data?.occupation).toString()
                    // date
                    binding.etDate.setText(result.data?.date).toString()
                    // marcar RadioButton de genere
                    val gene = result.data?.gender.toString()
                    if (gene == "Femenino") {
                        binding.rdFemale.isChecked = true
                    }
                    if (gene == "Masculino") {
                        binding.rdMale.isChecked = true
                    } else {
                        binding.other.isChecked = true
                    }
                }
                is FetchResult.Error -> {
                    // Maneja el error aquí
                }
            }
        }
    }



}