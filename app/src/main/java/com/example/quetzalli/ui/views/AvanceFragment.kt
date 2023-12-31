package com.example.quetzalli.ui.views

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentAvanceBinding
import com.example.quetzalli.ui.adapters.TestAdapter
import com.example.quetzalli.viewmodel.TestVM
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AvanceFragment : Fragment() {
    private lateinit var binding: FragmentAvanceBinding
    private lateinit var navController: NavController
    private val userVM: UserVM by viewModels()
    private val testVM: TestVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAvanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        testVM.tests.observe(viewLifecycleOwner) { tests ->
            // Crea un nuevo TestAdapter con los datos de las pruebas
            val adapter = tests?.let { TestAdapter(it) }
            // Asigna el adaptador a tu RecyclerView
            binding.rvTest.adapter = adapter
            binding.progressIndicator.visibility = View.GONE
        }

        binding.progressIndicator.visibility = View.VISIBLE
        // Obtiene los datos de las pruebas
        testVM.getTestData()

        // Obtiene la fecha actual del dispositivo
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        // Obtiene la cadena de recursos con el salto de línea
        val description = getString(R.string.avance_description)

        // Formatea la cadena con la fecha actual
        val text = String.format(description, currentDate)

        // Crea un SpannableStringBuilder a partir de la cadena formateada
        val spannable = SpannableStringBuilder(text)

        // Encuentra el inicio y el final de la fecha en la cadena
        val start = text.indexOf(currentDate)
        val end = start + currentDate.length

        // Aplica negritas a la fecha
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        // Configura el TextView con el SpannableStringBuilder
        binding.tvDescription.text = spannable

        binding.progressIndicator.visibility = View.VISIBLE
        // Obtiene los datos de las pruebas
        testVM.getTestData()

    }

    private fun init() {
        navController = findNavController()

        val currentUser = userVM.getCurrentUser()
        val photoURL = currentUser?.photoUrl.toString()

        Glide.with(this).load(photoURL).into(binding.ivProfile)

    }
}