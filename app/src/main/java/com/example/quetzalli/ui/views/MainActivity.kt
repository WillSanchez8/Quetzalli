package com.example.quetzalli.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val topLevelDestinatios = setOf(R.id.sesion, R.id.avance, R.id.perfil)
    private val test = setOf(R.id.memoryTest, R.id.calculoTest, R.id.ubicacionTest, R.id.load)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        registerEvents()
    }

    private fun init() {
        //Configuración general de navegación
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        //Configuración del bottom navigation
        binding.bottomNav.setupWithNavController(navController)

        setSupportActionBar(binding.topAppBar)

        //Configuración del action bar
        val config = AppBarConfiguration(topLevelDestinatios)
        setupActionBarWithNavController(navController, config)
    }

    private fun registerEvents() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.notification -> {
                    navController.navigate(R.id.notificacionesFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.countdown || destination.id == R.id.load) {
                // Estás en CountdownFragment, oculta la Toolbar y la BottomNavigationView
                binding.topAppBar.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
            } else if (test.contains(destination.id)) {
                // Estás en MemoryTestFragment, muestra la Toolbar y la BottomNavigationView, pero oculta el botón de retroceso
                binding.topAppBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
                binding.topAppBar.navigationIcon = null
            } else if (topLevelDestinatios.contains(destination.id)) {
                // Estás en un fragmento de nivel superior, muestra la Toolbar y la BottomNavigationView
                binding.topAppBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
                // Oculta el icono de navegación
                binding.topAppBar.navigationIcon = null
            } else {
                // No estás en un fragmento de nivel superior, muestra la Toolbar y la BottomNavigationView
                binding.topAppBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
                // Muestra el icono de navegación
                binding.topAppBar.setNavigationIcon(R.drawable.backbutton)
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            if (!topLevelDestinatios.contains(navController.currentDestination?.id)) {
                navController.navigateUp()
            }
        }

        //Configuración del bottom navigation
        binding.bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.sesion -> {
                    navController.navigate(R.id.sesion)
                }
                R.id.avance -> {
                    navController.navigate(R.id.avance)
                }
                R.id.perfil -> {
                    navController.navigate(R.id.perfil)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        when (navController.currentDestination?.id) {
            R.id.avance -> {
                menu.findItem(R.id.notification).isVisible = true
            }
            R.id.perfil -> {
                menu.findItem(R.id.notification).isVisible = true
            }
            else -> {
                menu.findItem(R.id.notification).isVisible = false
            }
        }
        return true

    }
    override fun onBackPressed() {
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.sesion) {
            // Cierra la aplicación
            finish()
        } else if (test.contains(currentDestination)) {
            // No hagas nada
        } else {
            super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)
        return true
    }
}
