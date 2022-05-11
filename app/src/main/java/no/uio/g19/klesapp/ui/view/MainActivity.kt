package no.uio.g19.klesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.view.fragments.location.ChooseLocationFragment
import no.uio.g19.klesapp.ui.view.fragments.home.HomeFragment
import no.uio.g19.klesapp.ui.view.fragments.settings.SettingsFragment

/**
 * This class is an [AppCompatActivity] which provides the base window in which the app draws its UI.
 * It contains logic behind the application's navigation menu, in which the users are able to switch
 * between the three main fragments: [HomeFragment], [ChooseLocationFragment], [SettingsFragment].
 */
class MainActivity : AppCompatActivity() {

    /**
     * During this function [HomeFragment] is set as the default fragment.
     * It also contains a navigationItem listener which changes the fragment based on the users
     * choice.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val chooseLocationFragment = ChooseLocationFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(homeFragment)

        val menu = findViewById<BottomNavigationView>(R.id.button_navigation)

        menu.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_place -> makeCurrentFragment(chooseLocationFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }

    }
}

/**
 * This function contains the logic behind fragment changes in onCreate.
 */
fun AppCompatActivity.makeCurrentFragment(fragment: Fragment) =
    supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragment_container, fragment)
        commit()
    }