package com.app.spaceitemdeliveryapp.ui.main.space_travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem
import android.view.View
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.databinding.FragmentSpaceTravelBinding
import com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.favorites.FavoritesFragment
import com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets.PlanetsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpaceTravelFragment : Fragment(R.layout.fragment_space_travel), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentSpaceTravelBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpaceTravelBinding.bind(view)

        binding.navigationView.setOnNavigationItemSelectedListener(this)
        initView()
    }

    private fun initView() {
        val planetsFragment = PlanetsFragment()
        openFragment(planetsFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_planets -> {
                val planetsFragment = PlanetsFragment()
                openFragment(planetsFragment)
                return true
            }
            R.id.navigation_favorites -> {
                val favoritesFragment = FavoritesFragment()
                openFragment(favoritesFragment)
                return true
            }
        }
        return false
    }

    private fun openFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment).addToBackStack(null)
        }.commit()
    }
}