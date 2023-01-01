package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.ui.base.BaseActivity
import com.litholr.prolearner.utils.PLToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel
        get() = MainViewModel()

//    lateinit var navHostFragment: NavHostFragment
//    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
        setListeners()
        val logo = ImageView(this)
        logo.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.appbar_logo))
        binding.appbar.addView(logo)
    }

    private fun setListeners() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    navigateTo(R.id.toHomeFragment)
                    true
                }
                R.id.search -> {
                    navigateTo(R.id.toBookSearchFragment)
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun navigateTo(actionId: Int) {
        navController.navigate(actionId)
    }
}


