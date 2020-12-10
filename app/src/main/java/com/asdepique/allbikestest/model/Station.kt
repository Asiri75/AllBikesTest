package com.asdepique.allbikestest.model

import com.google.android.gms.maps.model.LatLng

data class Station(
    val number : Int,
    val contractName : String,
    val name : String,
    val address : String,
    val position : LatLng,
    val banking : Boolean,
    val status : String,
    val lastUpdate : String,
    val overflow : Boolean,
    val capacity : Int,
    val nbMechanicalBikes : Int,
    val nbElectricalBikes : Int,
    val nbPlaces: Int
)
