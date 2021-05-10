package com.app.spaceitemdeliveryapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.data.room.planets.PlanetsDao

import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.data.room.spacecraft.SpacecraftsDao
import com.app.spaceitemdeliveryapp.utils.Constants.DATABASE_NAME

@Database(entities = [Spacecrafts::class, Planets::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spacecraftsDao(): SpacecraftsDao
    abstract fun planetsDao(): PlanetsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
