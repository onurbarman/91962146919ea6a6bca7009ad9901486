package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spaceitemdeliveryapp.data.model.planets.PlanetsModel
import com.app.spaceitemdeliveryapp.data.repository.PlanetsRepository
import com.app.spaceitemdeliveryapp.data.room.planets.LocalPlanetsRepository
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.data.room.spacecraft.SpacecraftsRepository
import com.app.spaceitemdeliveryapp.ui.custom.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetsViewModel @Inject constructor(
    private val planetsRepository: PlanetsRepository,
    private val spacecraftsRepository: SpacecraftsRepository,
    private val repository: LocalPlanetsRepository
) : ViewModel() {

    val postPlanets: MutableLiveData<PlanetsModel> by lazy {
        MutableLiveData<PlanetsModel>()
    }

    fun getPlanets() {
        viewModelScope.launch {
            val retrofitPost = planetsRepository.getPlanets()
            if (retrofitPost.data!=null) {
                postPlanets.postValue(retrofitPost.data)
                Log.d("planets_response",retrofitPost.data.toString())
            }
        }
    }

    val postLocalPlanets: MutableLiveData<List<Planets>> by lazy {
        MutableLiveData<List<Planets>>()
    }

    fun getLocalPlanets() {
        viewModelScope.launch {
            val planets : List<Planets>? =  repository.getPlanets()
            if(planets!=null){
                postLocalPlanets.postValue(planets)
                Log.d("planets_response",planets.toString())
            }
        }

    }

    val postSpacecraft: SingleLiveEvent<Spacecrafts?> by lazy {
        SingleLiveEvent()
    }

    fun getSpacecraft(id : Long) {
        viewModelScope.launch {
            val spacecraft : Spacecrafts? =  spacecraftsRepository.getSpacecraft(id)
            postSpacecraft.postValue(spacecraft)
            Log.d("spacecraft_response",spacecraft.toString())
        }

    }

    val postPlanet: SingleLiveEvent<Planets?> by lazy {
        SingleLiveEvent()
    }

    fun getPlanet(id : Long) {
        viewModelScope.launch {
            val planet : Planets? =  repository.getPlanet(id)
            postPlanet.postValue(planet)
        }

    }

    val postDbResponse: SingleLiveEvent<Long> by lazy {
        SingleLiveEvent()
    }

    fun addPlanet(planet: Planets) {
        viewModelScope.launch {
            val response = repository.addPlanet(planet)
            postDbResponse.postValue(response)
            Log.d("Planet","Planet added")
        }

    }

    fun updatePlanet(planet: Planets) {
        viewModelScope.launch {
            val response = repository.updatePlanet(planet)
            postDbResponse.postValue(response.toLong())
            Log.d("Planet",response.toString())
        }

    }

    val postSpacecraftDbResponse: SingleLiveEvent<Long> by lazy {
        SingleLiveEvent()
    }

    fun updateSpacecraft(spacecraft: Spacecrafts) {
        viewModelScope.launch {
            val response = spacecraftsRepository.updateSpacecraft(spacecraft)
            postSpacecraftDbResponse.postValue(response.toLong())
            Log.d("Spacecraft",response.toString())
        }

    }

    val postClearTable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun clearPlanets() {
        viewModelScope.launch {
            repository.clearPlanets()
            postClearTable.postValue(true)
            Log.d("Planet","Planets cleared")
        }

    }
}