package com.app.spaceitemdeliveryapp.data.room.planets

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPlanetsRepository @Inject constructor(
    private val planetsDao: PlanetsDao
) {

    suspend fun getPlanets(): List<Planets>? {
        return planetsDao.getAllPlanets()
    }

    suspend fun getPlanet(id: Long): Planets? {
        return planetsDao.getPlanet(id)
    }

    suspend fun addPlanet(planet: Planets): Long {
        return planetsDao.addPlanet(planet)
    }

    suspend fun updatePlanet(planet: Planets): Int {
        planet.run {
            return planetsDao.updatePlanet(id,stock, need, isComplete, isFavorite)
        }

    }

    suspend fun clearPlanets() {
        planetsDao.clearPlanetsTable()
    }
}