package com.example.quetzalli.ui.views

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quetzalli.R
import com.example.quetzalli.data.models.User
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.databinding.FragmentRegisterBinding
import com.example.quetzalli.viewmodel.UserVM
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val userVM: UserVM by viewModels()
    private lateinit var navController: NavController
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

    private fun init() {
        navController = findNavController()

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

        val spannableString = SpannableString("Acepto los Términos y Condiciones de uso")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navController.navigate(R.id.action_registerFragment_to_termsOfConditionsFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true // Esto hará que el texto esté subrayado
            }
        }

        spannableString.setSpan(clickableSpan, 11, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.cbTermsAndConditions.text = spannableString
        binding.cbTermsAndConditions.movementMethod = LinkMovementMethod.getInstance()
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

        // Valida el campo de género
        val genderId = binding.rgGenere.checkedRadioButtonId
        if (genderId == -1) {
            // Muestra un mensaje de error si ningún botón de radio está seleccionado
            Snackbar.make(binding.root, "Por favor, selecciona un género", Snackbar.LENGTH_SHORT)
                .show()
            isValid = false
        }

        val antecedents = binding.rgNeurodegenerative.checkedRadioButtonId
        if (antecedents == -1) {
            // Muestra un mensaje de error si ningún botón de radio está seleccionado
            Snackbar.make(binding.root, "Por favor, selecciona un respuesta", Snackbar.LENGTH_SHORT)
                .show()
            isValid = false
        }

        // Valida el checkbox de términos y condiciones
        if (!binding.cbTermsAndConditions.isChecked) {
            // Muestra un mensaje de error si el checkbox no está marcado
            Snackbar.make(
                binding.root,
                "Debes aceptar los términos y condiciones",
                Snackbar.LENGTH_SHORT
            ).show()
            isValid = false
        }

        return isValid
    }

    private fun registerEvents() {
        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                val name = binding.tilName.editText?.text.toString()
                val date = binding.tilDate.editText?.text.toString()
                val occupation = binding.tilOcupation.editText?.text.toString()
                val gender = when (binding.rgGenere.checkedRadioButtonId) {
                    R.id.rd_male -> "Male"
                    R.id.rd_female -> "Female"
                    else -> ""
                }
                val antecedents = when (binding.rgNeurodegenerative.checkedRadioButtonId) {
                    R.id.rdYes -> "Yes"
                    R.id.rdNo -> "No"
                    else -> ""
                }

                val currentUser = userVM.getCurrentUser()
                val user = User(
                    id = currentUser?.uid,
                    email = currentUser?.email,
                    name = name,
                    date = date,
                    occupation = occupation,
                    gender = gender,
                    antecedents = antecedents
                )

                userVM.registerUser(user) // Llama a registerUser con el objeto User

                userVM.registerResult.observe(viewLifecycleOwner) { result ->
                    if (result is FetchResult.Success) {
                        // Si el registro fue exitoso, navega a MainActivity
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    } else if (result is FetchResult.Error) {
                        // Si el registro falló, muestra un mensaje de error
                        Snackbar.make(
                            binding.root,
                            "Error al registrar el usuario, intente de nuevo más tarde",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }
    }


}