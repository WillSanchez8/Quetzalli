package com.example.quetzalli.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.data.models.User
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.databinding.FragmentDataPersonalBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataPersonalFragment : Fragment() {

    private lateinit var binding: FragmentDataPersonalBinding
    private val userVM: UserVM by viewModels()
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
        val currentUser = userVM.getCurrentUser()
        val photoURL = currentUser?.photoUrl.toString()

        Glide.with(this).load(photoURL).into(binding.photoUser)
        binding.tilDate.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona tu fecha de nacimiento")
                .build()

            datePicker.show(childFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                // Aquí puedes manejar la fecha seleccionada
                val dateString = datePicker.headerText
                binding.etDate.setText(dateString)
                binding.tilDate.error = null // Esto eliminará el mensaje de error
            }
        }
        // Obtiene el usuario por su id
        userVM.getUserById(currentUser?.uid ?: "").observe(viewLifecycleOwner) { fetchResult ->
            fetchResult?.let { result ->
                when (result) {
                    is FetchResult.Success -> {
                        val user = result.data
                        // Establece los datos del usuario en los campos correspondientes
                        binding.etName.setText(user?.name)
                        binding.etDate.setText(user?.date)
                        binding.etOcupation.setText(user?.occupation)
                    }
                    is FetchResult.Error -> {
                        Snackbar.make(binding.root, result.exception.message ?: "Error al obtener los datos del usuario", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val occupation = resources.getStringArray(R.array.occupations)
        val adapter = ArrayAdapter(requireContext(), R.layout.ocupation_item, occupation)
        val autoCompleteTextView = binding.tilOcupation.editText as? MaterialAutoCompleteTextView
        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun validateFields(): Boolean {
        var isValid = true

        val fields = listOf(
            binding.tilName to "El nombre es obligatorio",
            binding.tilDate to "La fecha es obligatoria",
            binding.tilOcupation to "La ocupación es obligatoria"
        )

        for ((field, errorMessage) in fields) {
            if (field.editText?.text.toString().isEmpty()) {
                field.error = errorMessage
                isValid = false
            } else {
                field.error = null
            }
        }


        return isValid
    }

    private fun registerEvents() {
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                val currentUser = userVM.getCurrentUser()
                val user = User(
                    id = currentUser?.uid,
                    name = binding.etName.text.toString(),
                    date = binding.etDate.text.toString(),
                    occupation = binding.etOcupation.text.toString()
                )

                userVM.updateUser(user).observe(viewLifecycleOwner) { fetchResult ->
                    fetchResult?.let { result ->
                        when (result) {
                            is FetchResult.Success -> {
                                Snackbar.make(binding.root, "Usuario actualizado con éxito", Snackbar.LENGTH_SHORT).show()
                            }
                            is FetchResult.Error -> {
                                Snackbar.make(binding.root, result.exception.message ?: "Error al actualizar el usuario", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

}