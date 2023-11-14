package com.example.quetzalli.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentRegisterBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val userVM : UserVM by viewModels()
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

        // Valida el campo de género
        val genderId = binding.rgGenere.checkedRadioButtonId
        if (genderId == -1) {
            // Muestra un mensaje de error si ningún botón de radio está seleccionado
            Snackbar.make(binding.root, "Por favor, selecciona un género", Snackbar.LENGTH_SHORT).show()
            isValid = false
        }

        // Valida el checkbox de términos y condiciones
        if (!binding.cbTermsAndConditions.isChecked) {
            // Muestra un mensaje de error si el checkbox no está marcado
            Snackbar.make(binding.root, "Debes aceptar los términos y condiciones", Snackbar.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun registerEvents() {
        binding.btnRegister.setOnClickListener {
            val name = binding.tilName.editText?.text.toString()
            val date = binding.tilDate.editText?.text.toString()
            val occupation = binding.tilOcupation.editText?.text.toString()

            // Obtiene el género seleccionado en el RadioGroup
            val selectedGenderId = binding.rgGenere.checkedRadioButtonId
            val selectedGenderButton = view?.findViewById<RadioButton>(selectedGenderId)
            val gender = selectedGenderButton?.text.toString()

            if (validateFields()) {
                // llamada al método createUser del UserVM
                userVM.createUser(name, gender, date, occupation)

                userVM.user.observe(viewLifecycleOwner) { user ->
                    if (user != null) {
                        // Guarda en SharedPreferences que el usuario ha iniciado sesión
                        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        sharedPreferences?.edit()?.apply {
                            putBoolean("UserChoice", true)
                            apply()
                        }

                        // Muestra un Snackbar para informar al usuario que la cuenta se ha creado correctamente
                        Snackbar.make(binding.root, "Cuenta creada correctamente.", Snackbar.LENGTH_LONG).show()
                        // Navega a MainActivity
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
        }
    }
}