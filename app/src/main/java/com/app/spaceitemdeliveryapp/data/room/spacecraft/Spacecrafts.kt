package com.app.spaceitemdeliveryapp.data.room.spacecraft

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spacecrafts")
data class Spacecrafts (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "durability") val durability: Int,
    @ColumnInfo(name = "speed") val speed: Int,
    @ColumnInfo(name = "capacity") val capacity: Int,
    @ColumnInfo(name = "spacesuitCount") val spacesuitCount: Int,
    @ColumnInfo(name = "universalSpaceTime") val universalSpaceTime: Int,
    @ColumnInfo(name = "durabilityTime") val durabilityTime: Int,
    @ColumnInfo(name = "currentLocationId") val currentLocationId: Long,
    @ColumnInfo(name = "currentLocation") val currentLocation: String,
)