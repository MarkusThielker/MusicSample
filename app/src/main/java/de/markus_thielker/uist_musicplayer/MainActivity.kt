package de.markus_thielker.uist_musicplayer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import de.markus_thielker.uist_musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup view binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id == R.id.navigationFragmentPlayer) {
                supportActionBar?.hide()
                binding.floatingActionButton.hide()
                binding.bottomNavigation.visibility = View.GONE
            } else {
                supportActionBar?.show()
                binding.floatingActionButton.show()
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        // connect action bar with navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigationFragmentHome,
                R.id.navigationFragmentSearch,
                R.id.navigationFragmentFavorites
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // connect bottom navigation with navController
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.setupWithNavController(navController)

        // Floating button
        val fab = binding.floatingActionButton
        fab.setOnClickListener { view ->
            navController.navigate(R.id.action_global_navigationFragmentPlayer)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}