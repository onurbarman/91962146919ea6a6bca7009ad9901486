package com.app.spaceitemdeliveryapp.di

import android.content.Context
import com.app.spaceitemdeliveryapp.data.repository.PlanetsRepository
import com.app.spaceitemdeliveryapp.data.room.AppDatabase
import com.app.spaceitemdeliveryapp.data.room.planets.PlanetsDao
import com.app.spaceitemdeliveryapp.data.room.spacecraft.SpacecraftsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlanetsDao(appDatabase: AppDatabase): PlanetsDao {
        return appDatabase.planetsDao()
    }

    @Provides
    fun provideSpacecraftsDao(appDatabase: AppDatabase): SpacecraftsDao {
        return appDatabase.spacecraftsDao()
    }

    @Singleton
    @Provides
    fun providePlanetsRepository(): PlanetsRepository {
        return PlanetsRepository()
    }
}
