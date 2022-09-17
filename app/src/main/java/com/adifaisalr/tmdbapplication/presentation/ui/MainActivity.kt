package com.adifaisalr.tmdbapplication.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adifaisalr.tmdbapplication.MobileNavigationDirections
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.databinding.ActivityMainBinding
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        binding.toolbar.setOnMenuItemClickListener {
            val action = MobileNavigationDirections.actionGlobalSearchFragment()
            navController.safeNavigate(action)
            true
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        viewModel.title.observe(this) { title ->
            binding.toolbar.title = title
        }
        viewModel.navigationIcon.observe(this) { nav ->
            binding.toolbar.navigationIcon = nav
            binding.toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
        viewModel.showBottomNav.observe(this) { isShow ->
            binding.navView.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }
}