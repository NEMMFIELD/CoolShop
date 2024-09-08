package com.example.coolshop

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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
    private val bottomNavigationView by lazy { binding.bottomNavBar }
    private val badge by lazy { bottomNavigationView.getOrCreateBadge(R.id.cartFragment) }
    @Inject
    lateinit var cartRepository: CartRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
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
}
