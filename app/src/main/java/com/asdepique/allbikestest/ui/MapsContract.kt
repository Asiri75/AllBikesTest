package com.asdepique.allbikestest.ui

import com.asdepique.allbikestest.model.Station

interface MapsContract {
    fun onStationsResult(stations: List<Station>)
    fun onStationsRequestError()
}