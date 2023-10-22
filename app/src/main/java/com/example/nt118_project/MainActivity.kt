package com.example.nt118_project

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nt118_project.Fragments.BillFragment
import com.example.nt118_project.Fragments.HomeFragment
import com.example.nt118_project.Fragments.SearchFragment
import com.example.nt118_project.Fragments.UserFragment
import com.example.nt118_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    internal var selectedFragment: Fragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
            }
            R.id.nav_search -> {
                selectedFragment = SearchFragment()
            }
            R.id.nav_bill -> {
                selectedFragment = BillFragment()
            }
            R.id.nav_user -> {
                selectedFragment = UserFragment()
            }
        }
        if (selectedFragment!=null){
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,selectedFragment!!
            ).commit()
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager
                .LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

}