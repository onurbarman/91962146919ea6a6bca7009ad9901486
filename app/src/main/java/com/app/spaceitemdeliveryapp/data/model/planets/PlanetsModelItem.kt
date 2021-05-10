package com.app.spaceitemdeliveryapp.data.model.planets

data class PlanetsModelItem(
    val capacity: Int,
    val coordinateX: Double,
    val coordinateY: Double,
    val name: String,
    val need: Int,
    val stock: Int
)