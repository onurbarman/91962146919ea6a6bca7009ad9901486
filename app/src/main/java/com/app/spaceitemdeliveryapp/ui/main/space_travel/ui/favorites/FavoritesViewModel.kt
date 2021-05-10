package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spaceitemdeliveryapp.data.room.planets.LocalPlanetsRepository
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.ui.custom.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: LocalPlanetsRepository
) : ViewModel() {
    val postLocalPlanets: MutableLiveData<List<Planets>> by lazy {
        MutableLiveData<List<Planets>>()
    }

    fun getLocalPlanets() {
        viewModelScope.launch {
            val planets : List<Planets>? =  repository.getPlanets()
            if(planets!=null){
                postLocalPlanets.postValue(planets)
            }
        }

    }

    val postDbResponse: SingleLiveEvent<Long> by lazy {
        SingleLiveEvent()
    }

    fun updatePlanet(planet: Planets) {
        viewModelScope.launch {
            val response = repository.updatePlanet(planet)
            postDbResponse.postValue(response.toLong())
            Log.d("Planet",response.toString())
        }

    }
}