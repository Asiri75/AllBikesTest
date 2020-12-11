package com.asdepique.allbikestest.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asdepique.allbikestest.R
import com.asdepique.allbikestest.model.Station
import com.asdepique.allbikestest.ui.details.StationDetailsActivity
import com.asdepique.allbikestest.ui.details.StationDetailsActivity.Companion.STATION_KEY
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsContract,
    GoogleMap.OnMarkerClickListener {

    companion object {
        private const val DEFAULT_ZOOM = 11f
        private const val NO_CURRENT_LOCATION_ZOOM = 5f
        private val FRANCE_LOCATION = LatLng(46.839130, 2.443642)
    }

    private lateinit var fusedLocationPC: FusedLocationProviderClient
    private var gMap: GoogleMap? = null
    private lateinit var presenter: MapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = MapsPresenter(this)

        fusedLocationPC = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        presenter.getAllStations()
        gMap?.setOnMarkerClickListener(this)
        zoomToCurrentLocationWithPermissionCheck()
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun zoomToCurrentLocation() {
        gMap?.isMyLocationEnabled = true
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationPC.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations
                .takeIf {
                    it.isNotEmpty()
                }?.last()?.let { lastLocation ->
                    gMap?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastLocation.latitude,
                                lastLocation.longitude
                            ),
                            DEFAULT_ZOOM
                        )
                    )
                }

        }
    }


    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showDeniedForLocationPermission() {
        Toast.makeText(this, getString(R.string.toast_location_denied), Toast.LENGTH_SHORT).show()
        showFranceMap()
    }


    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showNeverAskForLocationPermission() {
        showFranceMap()
        Toast.makeText(
            this,
            getString(R.string.toast_location_never_ask),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun showFranceMap() {
        gMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                FRANCE_LOCATION,
                NO_CURRENT_LOCATION_ZOOM
            )
        )
    }

    private fun fillTheMap(stations: List<Station>) {
        stations.forEach {
            gMap?.addMarker(
                MarkerOptions()
                    .position(it.getPosition())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_marker))
            )?.apply {
                tag = it
            }
        }
    }

    override fun onStationsResult(stations: List<Station>) {
        fillTheMap(stations)
    }


    override fun onStationsRequestError() {
        Toast.makeText(this, getString(R.string.stations_result_error), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return (marker?.tag as? Station)?.let {
            startActivity(
                Intent(
                    this,
                    StationDetailsActivity::class.java
                ).apply {
                    putExtra(STATION_KEY, it)
                }
            )
            true
        } ?: false
    }
}