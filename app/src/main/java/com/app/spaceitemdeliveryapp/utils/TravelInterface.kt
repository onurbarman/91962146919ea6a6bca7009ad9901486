package com.app.spaceitemdeliveryapp.utils

import com.app.spaceitemdeliveryapp.data.room.planets.Planets

interface TravelInterface {
    fun travelClick(distance: Int,planet: Planets)
    fun favoriteClick(planet: Planets)
}