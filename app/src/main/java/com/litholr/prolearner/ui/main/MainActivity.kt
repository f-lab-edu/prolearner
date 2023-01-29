package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.BuildCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.databinding.AppbarBookBinding
import com.litholr.prolearner.databinding.AppbarSearchBinding
import com.litholr.prolearner.ui.base.BaseActivity
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
            binding.appbar.removeAllViews()
            when(it) {
                MainViewModel.BottomNav.HOME -> toHome()
                MainViewModel.BottomNav.SEARCH -> toSearch()
                MainViewModel.BottomNav.PROFILE -> {}
                MainViewModel.BottomNav.BOOK -> toBook()
                else -> {}
            }
        }
    }

    private fun toHome() {
        val logoImage = ImageView(this@MainActivity).apply {
            setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.appbar_logo))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(12 * 4, 0, 0,0)
                gravity = Gravity.CENTER_VERTICAL
            }
        }
        binding.appbar.addView(logoImage)
        navController.navigate(R.id.toHomeFragment)
    }

    private fun toSearch() {
        val appbarSearchBinding = AppbarSearchBinding.inflate(LayoutInflater.from(this@MainActivity)).apply {
            root.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            search.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s!!.isEmpty()) {
                        viewModel.query.postValue(s.toString())
                    }
                }
            })
            button.setOnClickListener {
                viewModel.isInitial.postValue(true)
                viewModel.searchBook()
            }
        }
        binding.appbar.addView(appbarSearchBinding.root)
        navController.navigate(R.id.toSearchFragment)
    }

    private fun toBook() {
        val appbarBookBinding = AppbarBookBinding.inflate(LayoutInflater.from(this@MainActivity)).apply {
            root.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            back.setOnClickListener {
                toSearchBack()
            }
            save.setOnClickListener {

            }
        }
        binding.appbar.addView(appbarBookBinding.root)
        binding.bottomNavigationBar.visibility = View.GONE
        navController.navigate(R.id.toBookFragment)
    }

    private fun toSearchBack() {
        binding.bottomNavigationBar.visibility = View.VISIBLE
        viewModel.bottomNav.value = MainViewModel.BottomNav.SEARCH
    }
}