package com.asdepique.allbikestest.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Station(
    val number : Int,
    val contractName : String,
    val name : String,
    val address : String,
    val latitude : Double,
    val longitude : Double,
    val banking : Boolean,
    val status : String,
    val lastUpdate : String,
    val overflow : Boolean,
    val capacity : Int,
    val nbMechanicalBikes : Int,
    val nbElectricalBikes : Int,
    val nbPlaces: Int
) : Serializable {
    fun isOpen() : Boolean {
        return status == "OPEN"
    }

    fun getPosition() : LatLng {
        return LatLng(latitude, longitude)
    }
}
