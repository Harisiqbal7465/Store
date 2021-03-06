package com.example.store.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.store.R
import com.example.store.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNavigationView

        lifecycleScope.launchWhenResumed {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.mainListFragment, R.id.stockFragment, R.id.expenseFragment, R.id.notificationFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                    }
                }
            }
        }

        appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.mainListFragment,
                R.id.stockFragment,
                R.id.expenseFragment,
                R.id.notificationFragment,
            )
        ).build()
        setupNavHost()
        bottomNavigationView.setupWithNavController(navController)
    }
    private fun setupNavHost(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        navController = navHostFragment.navController
    }
    fun setupActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupNavHost()
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(AppBarConfiguration(navController.graph))
                || super.onSupportNavigateUp()
    }

}