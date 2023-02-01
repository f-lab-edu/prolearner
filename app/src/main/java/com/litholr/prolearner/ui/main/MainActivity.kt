package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.ui.base.BaseActivity
import com.litholr.prolearner.utils.PLToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        setNavigation()
        viewModel.setOnNavigationItemSelectedListener(binding.bottomNavigationBar)
        setListeners()
        setObservables()
    }

    private fun setNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
    }

    private fun setObservables() {
        viewModel.page.observe(this) {
            viewModel.searchBook()
        }
        viewModel.bottomNav.observe(this) {
            binding.bottomNav = it
            when(it) {
                MainViewModel.BottomNav.HOME -> navController.navigate(R.id.toHomeFragment)
                MainViewModel.BottomNav.SEARCH -> navController.navigate(R.id.toSearchFragment)
                MainViewModel.BottomNav.PROFILE -> {}
                MainViewModel.BottomNav.BOOK -> navController.navigate(R.id.toBookFragment)
                else -> {}
            }
        }
        viewModel.query.observe(this) {
            Log.d(this.javaClass.simpleName, "query : $it")
        }
    }

    private fun setListeners() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s!!.isEmpty()) {
                    Log.d(this.javaClass.simpleName, "s : $s")
                    viewModel.query.postValue(s.toString())
                }
            }
        })
        binding.searchButton.setOnClickListener {
            viewModel.onSearchButtonClick()
        }
        viewModel.setPLToastListener(object: MainViewModel.PLToastListener {
            override fun printToast(message: String, term: Int) {
                PLToast.makeToast(this@MainActivity, message, term)
            }
        })
        binding.backButtonOfBook.setOnClickListener {
            viewModel.toSearchBack()
        }
    }
}