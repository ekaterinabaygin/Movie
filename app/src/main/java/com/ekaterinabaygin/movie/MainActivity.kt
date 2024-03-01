package com.ekaterinabaygin.movie

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.movie.databinding.ActivityMainBinding
import com.ekaterinabaygin.movie.feature.details.MovieDetailsFragment
import com.ekaterinabaygin.movie.feature.discovery.MovieDiscoveryFragment
import com.ekaterinabaygin.movie.feature.favourite.FavoritesFragment

class MainActivity : AppCompatActivity(),
    MovieDiscoveryFragment.DiscoveryActionCallback,
    FavoritesFragment.FavoritesActionCallback {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) replaceFragment(MovieDiscoveryFragment())
        setupBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun showDetails(movie: MoviePreview) {
        replaceFragment(
            fragment = MovieDetailsFragment.newInstance(movie.id),
            addToBackStack = true
        )
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> replaceFragment(MovieDiscoveryFragment.newInstance())
                R.id.nav_favorites -> replaceFragment(FavoritesFragment.newInstance())
            }
            true
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackCount = supportFragmentManager.backStackEntryCount
            binding.bottomNavigation.visibility = if (backStackCount > 0) GONE else VISIBLE
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .run { if (addToBackStack) addToBackStack(null) else this }
            .commit()
    }
}
