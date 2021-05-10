package com.app.spaceitemdeliveryapp.data.room.planets

import androidx.room.*

@Dao
interface PlanetsDao {

    @Query("SELECT * FROM planets")
    suspend fun getAllPlanets() : List<Planets>?

    @Query("SELECT * FROM planets where id= :id")
    suspend fun getPlanet(id : Long) : Planets?

    @Insert
    suspend fun addPlanet(planet: Planets) : Long

    @Query("UPDATE planets SET stock = :stock, need= :need,isComplete= :isComplete,isFavorite= :isFavorite WHERE id = :id")
    suspend fun updatePlanet(id: Long, stock: Int, need: Int, isComplete: Int, isFavorite: Int) : Int

    @Query("DELETE FROM planets")
    suspend fun clearPlanetsTable()
}