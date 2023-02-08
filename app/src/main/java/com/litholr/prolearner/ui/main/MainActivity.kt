package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.litholr.prolearner.R
import com.litholr.prolearner.data.local.AppDatabase
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.ui.base.BaseActivity
import com.litholr.prolearner.utils.PLToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    val db: RoomDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "localdb"
        ).build()
    }

    override fun onCreateBegin(savedInstanceState: Bundle?) {
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
                MainViewModel.BottomNav.HOME ->
                    navController.navigate(R.id.toHomeFragment)
                MainViewModel.BottomNav.SEARCH ->
                    navController.navigate(R.id.toSearchFragment)
                MainViewModel.BottomNav.BOOK ->
                    navController.navigate(R.id.toBookFragment)
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
    }

    private fun setListeners() {
        binding.searchButton.setOnClickListener {
            viewModel.onSearchButtonClick(binding.search.text.toString())
        }
        binding.backButtonOfBook.setOnClickListener {
            viewModel.toSearchBack()
        }
    }
}