package com.example.quetzalli.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init(){
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setTitleText("Selecciona una fecha")
        datePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
        val datePicker = datePickerBuilder.build()

        binding.tilDate.setEndIconOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            // Aquí puedes manejar la fecha seleccionada
            val dateString = datePicker.headerText
            binding.etDate.setText(dateString)
        }

        val ocupation = arrayOf("Estudiante", "Profesor", "Empleado", "Otro")
        val adapter = context?.let { ArrayAdapter(it, R.layout.ocupation_item, ocupation) }
        binding.etOcupation.setAdapter(adapter)

        binding.etOcupation.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

        }
    }

    private fun registerEvents(){

    }
}