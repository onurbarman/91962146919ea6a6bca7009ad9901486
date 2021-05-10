package com.app.spaceitemdeliveryapp.data.repository

import com.app.spaceitemdeliveryapp.data.model.planets.PlanetsModel
import com.app.spaceitemdeliveryapp.data.remote.Resource
import com.app.spaceitemdeliveryapp.data.remote.ServiceClientInstance
import com.app.spaceitemdeliveryapp.utils.Utils.safeApiCall

class PlanetsRepository {
    suspend fun getPlanets(): Resource<PlanetsModel> {
        return safeApiCall(call = { ServiceClientInstance.getInstance().api.getPlanets() })
    }
}