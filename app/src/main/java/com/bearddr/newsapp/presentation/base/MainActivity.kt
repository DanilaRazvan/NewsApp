package com.bearddr.newsapp.presentation.base

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration

  private lateinit var navHostFragment: NavHostFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)

    navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    navController = navHostFragment.findNavController()

    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.breakingNewsFragment,
        R.id.searchFragment,
        R.id.settingsFragment
      )
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    binding.bottomNavigation.setupWithNavController(navController)
  }

  override fun onSupportNavigateUp(): Boolean {
    return navHostFragment.findNavController().navigateUp()
        || super.onSupportNavigateUp()
  }

  override fun onResume() {
    super.onResume()
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
      val controller = window.insetsController
      if (controller != null) {
        controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    } else {
      window.decorView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
      )
    }
  }
}