package com.example.coolshop

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.coolshop.cart.domain.CartRepository
import com.example.coolshop.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var badge: BadgeDrawable? = null
    @Inject
    lateinit var cartRepository: CartRepository
    private var bottomNavigationView: BottomNavigationView? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var navHostFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupBottomNavigationView()
        lifecycleScope.launch {
            setBadge()
        }
    }

    private fun setupBottomNavigationView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = binding.bottomNavBar
        navHostFragment?.navController?.let { bottomNavigationView?.setupWithNavController(it) }
        badge = bottomNavigationView?.getOrCreateBadge(R.id.cartFragment)
    }

    private suspend fun setBadge() {
        cartRepository.flowCart.collect { list ->
            if (list.isEmpty()) badge?.isVisible = false
            else {
                badge?.isVisible = true
                badge?.badgeGravity = BadgeDrawable.TOP_END
                badge?.number = list.size
            }
        }
    }

}
