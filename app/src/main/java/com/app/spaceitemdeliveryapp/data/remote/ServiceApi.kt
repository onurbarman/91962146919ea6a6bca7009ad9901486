package com.app.spaceitemdeliveryapp.data.remote

import com.app.spaceitemdeliveryapp.data.model.planets.PlanetsModel
import retrofit2.Response
import retrofit2.http.GET

interface ServiceApi {
    //Get Planets
    @GET("e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    suspend fun getPlanets(): Response<PlanetsModel>
}