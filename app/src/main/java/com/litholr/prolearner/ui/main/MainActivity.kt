package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.unit.dp
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.databinding.AppbarSearchBinding
import com.litholr.prolearner.ui.base.BaseActivity
import com.litholr.prolearner.ui.test.BookSearchViewModel
import com.litholr.prolearner.utils.PLToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    companion object {
        val mainViewModel: MainViewModel
            get() = MainViewModel()
    }

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        setNavigation()
        toHome()
        setBottomNavigationBar()
        setObservables()
    }

    private fun setNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
    }

    private fun setBottomNavigationBar() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            binding.appbar.removeAllViews()
            when(it.itemId) {
                R.id.home -> {
                    toHome()
                    true
                }
                R.id.search -> {
                    toSearch()
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun setObservables() {
//        viewModel.query.observe(this) {
//            Log.d(this.javaClass.simpleName, "query : ${it}")
//            mainViewModel.searchBook(it)
//        }
//        viewModel.books.observe(this) {
//            Log.d(this.javaClass.simpleName, "setObservables() books -> ${it}")
//        }
        viewModel.page.observe(this) {
            viewModel.searchBook(viewModel.query.value!!, 10, it)
        }
    }

    private fun toHome() {
        binding.appbar.addView(
            ImageView(this@MainActivity).apply {
                setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.appbar_logo))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(12 * 4, 0, 0,0)
                    gravity = Gravity.CENTER_VERTICAL
                }
            }
        )
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
//            search.addTextChangedListener(object: TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                    Log.d(this.javaClass.simpleName, "beforeTextChanged : ${ s.toString() }")
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    Log.d(this.javaClass.simpleName, "onTextChanged : ${ s.toString() }")
//                    viewModel.query.postValue(s.toString())
//                }
//            })
            button.setOnClickListener {
//                mainViewModel.query.postValue(search.text.toString())
                viewModel.searchBook(search.text.toString())
            }
        }
        binding.appbar.addView(appbarSearchBinding.root)
        navController.navigate(R.id.toSearchFragment)
    }
}