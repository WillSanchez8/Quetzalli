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
import com.example.quetzalli.databinding.FragmentAvanceBinding
import com.example.quetzalli.ui.adapters.TestAdapter
import com.example.quetzalli.viewmodel.TestVM
import com.example.quetzalli.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AvanceFragment : Fragment() {
    private lateinit var binding: FragmentAvanceBinding
    private lateinit var navController: NavController
    private val userVM: UserVM by viewModels()
    private val testVM : TestVM by viewModels()

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

        testVM.lastTestPoint.observe(viewLifecycleOwner) { test ->
            // Calcula la fecha una semana después de la fecha obtenida
            var newDate = "-----"
            if (test != null) {
                val calendar = Calendar.getInstance()
                calendar.time = (test.date?.toDate() ?: calendar.add(Calendar.DATE, 6)) as Date
                newDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            }
            // Actualiza el TextView con la nueva fecha
            val text = getString(R.string.avance_description, newDate)
            binding.tvDescription.text = text
        }

        binding.progressIndicator.visibility = View.VISIBLE
        // Obtiene los datos de las pruebas
        testVM.getTestData()
        // Obtiene el último punto de prueba del usuario
        val userId = userVM.getCurrentUser()?.uid
        if (userId != null) {
            testVM.getLastTestPoint(userId)
        }
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