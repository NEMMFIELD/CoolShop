package com.example.coolshop

import android.os.Bundle
import android.view.Gravity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.coolshop.cart.domain.CartRepository
import com.example.coolshop.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeDrawable.BadgeGravity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var badge:BadgeDrawable
    @Inject
    lateinit var cartRepository: CartRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavBar
       bottomNavigationView.setupWithNavController(navController)
        badge = bottomNavigationView.getOrCreateBadge(R.id.cartFragment)
        lifecycleScope.launch {
                setBadge()
        }
    }
    private suspend fun setBadge() {
        cartRepository.flowCart.collect { list ->
            if (list.isEmpty()) badge.isVisible = false
            else {
                badge.isVisible  = true
                badge.badgeGravity = BadgeDrawable.TOP_END
                badge.number = list.size
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
