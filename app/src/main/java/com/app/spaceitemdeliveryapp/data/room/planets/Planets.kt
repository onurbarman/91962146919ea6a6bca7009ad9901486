package com.app.spaceitemdeliveryapp.data.room.planets

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets")
data class Planets (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "coordinateX") val coordinateX: Double,
    @ColumnInfo(name = "coordinateY") val coordinateY: Double,
    @ColumnInfo(name = "capacity") val capacity: Int,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "need") val need: Int,
    @ColumnInfo(name = "isComplete") val isComplete: Int,
    @ColumnInfo(name = "isFavorite") var isFavorite: Int,
)