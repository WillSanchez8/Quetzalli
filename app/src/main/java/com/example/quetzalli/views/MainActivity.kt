package com.example.quetzalli.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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

        //Configuración del back button
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (topLevelDestinatios.contains(destination.id)) {
                // Estás en un fragmento de nivel superior, oculta el icono de navegación
                binding.topAppBar.navigationIcon = null
            } else {
                // No estás en un fragmento de nivel superior, muestra el icono de navegación
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
            R.id.sesion -> {
                menu.findItem(R.id.notification).isVisible = true
                menu.findItem(R.id.share).isVisible = false
            }
            R.id.avance -> {
                menu.findItem(R.id.notification).isVisible = false
                menu.findItem(R.id.share).isVisible = true
            }
            else -> {
                menu.findItem(R.id.notification).isVisible = false
                menu.findItem(R.id.share).isVisible = false
            }
        }
        return true

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)
        return true
    }
}
