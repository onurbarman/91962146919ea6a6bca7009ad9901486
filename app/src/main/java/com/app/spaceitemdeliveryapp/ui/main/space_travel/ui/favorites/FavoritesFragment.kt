package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.databinding.FragmentFavoritesBinding
import com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets.PlanetsAdapter
import com.app.spaceitemdeliveryapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var binding: FragmentFavoritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)

        initView()
        listenLocalPlanetsData()
    }

    private fun initView() {
        favoritesAdapter = FavoritesAdapter(listOf()){isFavorite, planet ->  onFavoriteButtonClick(isFavorite,planet)}
        binding.run {
            recyclerViewFavorites.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            recyclerViewFavorites.adapter = favoritesAdapter
        }
        viewModel.run {
            if (Utils.getIsPlanetCalled(requireContext()))
                viewModel.getLocalPlanets()
        }
    }

    private fun listenLocalPlanetsData(){
        viewModel.postLocalPlanets.observe(requireActivity(), { planets ->
            planets?.let {
                val favoritesList : ArrayList<Planets> = arrayListOf()
                for(item in planets){
                    if (item.isFavorite==1)
                        favoritesList.add(item)
                }
                favoritesAdapter.updateFavorites(favoritesList)
            }
        })
    }

    private fun listenUpdateData(){
        viewModel.postDbResponse.observe(requireActivity(),{
            viewModel.getLocalPlanets()
        })
    }

    private fun onFavoriteButtonClick(favorite: Int, planet: Planets) {
        planet.run {
            val updatedPlanet = Planets(
                id = id,
                name = name,
                coordinateX = coordinateX,
                coordinateY = coordinateY,
                capacity = capacity,
                stock = stock,
                need = need,
                isComplete = isComplete,
                isFavorite = favorite
            )
            viewModel.updatePlanet(updatedPlanet)
        }
    }

}