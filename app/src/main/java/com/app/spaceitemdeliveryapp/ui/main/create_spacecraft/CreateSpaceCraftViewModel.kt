package com.app.spaceitemdeliveryapp.ui.main.create_spacecraft

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.spaceitemdeliveryapp.data.room.AppDatabase
import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.data.room.spacecraft.SpacecraftsRepository
import com.app.spaceitemdeliveryapp.ui.custom.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSpaceCraftViewModel @Inject constructor(
    private val repository: SpacecraftsRepository
) : ViewModel() {

    val postSpacecraft: SingleLiveEvent<Spacecrafts?> by lazy {
        SingleLiveEvent()
    }

    fun getSpacecraft(id : Long) {
        viewModelScope.launch {
            val spacecraft : Spacecrafts? =  repository.getSpacecraft(id)
            postSpacecraft.postValue(spacecraft)
        }

    }

    val postDbResponse: SingleLiveEvent<Long> by lazy {
        SingleLiveEvent()
    }

    fun addToSpacecrafts(spacecraft: Spacecrafts) {
        viewModelScope.launch {
            val response = repository.addSpacecraft(spacecraft)
            postDbResponse.postValue(response)
            Log.d("Spacecraft","Spacecraft added")
        }

    }

    fun updateSpacecraft(spacecraft: Spacecrafts) {
        viewModelScope.launch {
            val response = repository.updateSpacecraft(spacecraft)
            postDbResponse.postValue(response.toLong())
            Log.d("Spacecraft","Spacecraft updated")
        }

    }

    val postSpacecraftDelete: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun deleteFromSpacecrafts(id : String) {
        viewModelScope.launch {
            repository.deleteSpacecraft(id)
            postSpacecraftDelete.postValue(true)
            Log.d("Spacecraft","Spacecraft deleted")
        }

    }
}