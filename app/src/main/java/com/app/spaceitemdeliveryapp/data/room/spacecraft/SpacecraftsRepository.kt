package com.app.spaceitemdeliveryapp.data.room.spacecraft

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpacecraftsRepository @Inject constructor(
    private val spacecraftsDao: SpacecraftsDao
) {

    suspend fun getSpacecraft(id: Long): Spacecrafts? {
        return spacecraftsDao.getSpacecraft(id)
    }

    suspend fun addSpacecraft(spacecraft: Spacecrafts): Long {
        return spacecraftsDao.addSpacecraft(spacecraft)
    }

    suspend fun updateSpacecraft(spacecraft: Spacecrafts): Int {
        spacecraft.run {
            return spacecraftsDao.updateSpacecraft(id,spacesuitCount,universalSpaceTime,durabilityTime, currentLocationId,currentLocation)
        }
    }

    suspend fun deleteSpacecraft(id: String) {
        spacecraftsDao.deleteSpacecraft(id)
    }

    suspend fun clearSpacecrafts() {
        spacecraftsDao.clearSpacecraftTable()
    }
}