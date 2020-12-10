package com.asdepique.allbikestest.repositories

import com.asdepique.allbikestest.model.Station
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object StationRepository {

    private const val JCDECAUX_URL = "https://api.jcdecaux.com/vls/v3/"
    private val retrofit: Retrofit

    /**
     * We create a custom deserializer to get only the useful informations for the test
     */
    private var customDeserializer: JsonDeserializer<Station> =
        JsonDeserializer { json, _, _ ->
            val jsonObject = json.asJsonObject
            Station(
                number = jsonObject.get("number").asInt,
                contractName = jsonObject.get("contractName").asString,
                name = jsonObject.get("name").asString,
                address = jsonObject.get("address").asString,
                position = LatLng(
                    jsonObject.get("position").asJsonObject.get("latitude").asDouble,
                    jsonObject.get("position").asJsonObject.get("longitude").asDouble
                ),
                banking = jsonObject.get("banking").asBoolean,
                status = jsonObject.get("status").asString,
                lastUpdate = jsonObject.get("lastUpdate").asString,
                overflow = jsonObject.get("overflow").asBoolean,
                capacity = jsonObject.get("totalStands").asJsonObject.get("capacity").asInt,
                nbMechanicalBikes = jsonObject.get("totalStands").asJsonObject.get("availabilities").asJsonObject.get("mechanicalBikes").asInt,
                nbElectricalBikes = jsonObject.get("totalStands").asJsonObject.get("availabilities").asJsonObject.get("electricalBikes").asInt,
                nbPlaces = jsonObject.get("totalStands").asJsonObject.get("availabilities").asJsonObject.get("stands").asInt
            )
        }


    init {
        //Override the http client to add the API key
        val httpBuilder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
            addInterceptor(ApiKeyInterceptor())
        }
        // Add GSON deserializer to get only the data needed
        val gsonBuilder =  GsonBuilder().apply {
            registerTypeAdapter(Station::class.java, customDeserializer)
        }

        //Building the Retrofit client
        retrofit = Retrofit.Builder()
            .baseUrl(JCDECAUX_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpBuilder.build())
            .build()
    }

    /**
     * Get all the stations
     */
    fun createAllStationsCall() = retrofit.create(StationApi::class.java).getAllStations()

}