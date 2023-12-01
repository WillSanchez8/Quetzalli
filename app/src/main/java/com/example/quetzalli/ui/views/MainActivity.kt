package com.example.quetzalli.ui.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.View
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quetzalli.R
import com.example.quetzalli.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

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
                R.id.share -> {
                    val bitmap = takeScreenshot(binding.root)
                    shareBitmap(bitmap, this)
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

    fun takeScreenshot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun shareBitmap(bitmap: Bitmap, context: Context) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "screenshot.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        val fileUri = FileProvider.getUriForFile(context, "com.example.quetzalli.fileprovider", file)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "image/png"
        }
        context.startActivity(Intent.createChooser(intent, R.string.share_to.toString()))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)
        return true
    }
}
