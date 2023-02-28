package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.view.View
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
    override val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MainApplication).appDBRepository)
    }

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        viewModel.initDB(applicationContext)
        setNavigation()
        viewModel.setOnNavigationItemSelectedListener(binding.bottomNavigationBar)
        setListeners()
        setObservables()
    }

    fun setNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
    }

    fun setObservables() {
        viewModel.page.observe(this) {
            viewModel.searchBook()
        }
        viewModel.bottomNav.observe(this) {
            binding.bottomNav = it
            when(it) {
                MainViewModel.BottomNav.HOME -> {
                    binding.bottomNavigationBar.visibility = View.VISIBLE
                    navController.navigate(R.id.toHomeFragment)
                }
                MainViewModel.BottomNav.SEARCH -> {
                    binding.bottomNavigationBar.visibility = View.VISIBLE
                    navController.navigate(R.id.toSearchFragment)
                }
                MainViewModel.BottomNav.BOOK -> {
                    navController.navigate(R.id.toBookFragment)
                    binding.bottomNavigationBar.visibility = View.GONE
                }
                else -> {}
            }
        }
        viewModel.bookResultState.observe(this) {
            when(it) {
                MainViewModel.BookResultState.BOOK_INFO_NULL ->
                    PLToast.makeToast(this, "책의 정보를 불러오지 못했습니다.")
                MainViewModel.BookResultState.SEARCH_RESULT_NULL ->
                    PLToast.makeToast(this, "검색결과가 없습니다.")
                MainViewModel.BookResultState.BOOK_CONTENTS_NULL ->
                    PLToast.makeToast(this, "목차정보가 없습니다.")
                else -> {}
            }
        }
        viewModel.query.observe(this) {
            viewModel.searchBook()
        }
        viewModel.isSavedBookPage.observe(this) {
            binding.isSavedBookPage = it
        }
    }

    fun setListeners() {
        binding.searchButton.setOnClickListener {
            viewModel.onSearchButtonClick(binding.search.text.toString())
        }
        binding.backButtonOfBook.setOnClickListener {
            viewModel.toBack()
        }
        binding.save.setOnClickListener {
            viewModel.saveBook(this)
        }
    }
}