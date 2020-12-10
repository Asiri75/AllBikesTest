package com.asdepique.allbikestest.repositories

import com.asdepique.allbikestest.model.Station
import io.reactivex.Single
import retrofit2.http.GET

interface StationApi {
    @GET("stations")
    fun getAllStations(): Single<List<Station>>
}