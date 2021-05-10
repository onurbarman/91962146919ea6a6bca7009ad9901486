package com.app.spaceitemdeliveryapp.data.room.spacecraft

import androidx.room.*

@Dao
interface SpacecraftsDao {

    @Query("SELECT * FROM spacecrafts where id= :id")
    suspend fun getSpacecraft(id : Long) : Spacecrafts?

    @Insert
    suspend fun addSpacecraft(spacecraft: Spacecrafts): Long

    @Query("UPDATE spacecrafts SET  spacesuitCount= :spacesuitCount,universalSpaceTime= :universalSpaceTime,durabilityTime= :durabilityTime,currentLocationId= :currentLocationId,currentLocation= :currentLocation WHERE id = :id")
    suspend fun updateSpacecraft(id: Long,spacesuitCount: Int,universalSpaceTime: Int,durabilityTime: Int,currentLocationId: Long,currentLocation: String): Int

    @Query("DELETE FROM spacecrafts where id= :id")
    suspend fun deleteSpacecraft(id : String)

    @Query("DELETE FROM spacecrafts")
    suspend fun clearSpacecraftTable()
}