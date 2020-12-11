package com.asdepique.allbikestest.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asdepique.allbikestest.R
import com.asdepique.allbikestest.databinding.ActivityStationDetailsBinding
import com.asdepique.allbikestest.model.Station


class StationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStationDetailsBinding

    companion object {
        const val STATION_KEY = "STATION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        (intent.getSerializableExtra(STATION_KEY) as? Station)?.let {
            initUi(it)
        }
    }

    private fun initUi(station: Station) {
        binding.apply {
            stationName.text = station.name
            mechanicalBikes.text = station.nbMechanicalBikes.toString()
            electricalBikes.text = station.nbElectricalBikes.toString()
            places.text = station.nbPlaces.toString()
            address.text = station.address
            open.text = getString(if (station.isOpen()) R.string.yes else R.string.no)
            banking.text = getString(if (station.banking) R.string.yes else R.string.no)

            itineraryButton.setOnClickListener {
                launchGoogleItinerary(station)
            }
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.station_details_title)
        }
    }

    private fun launchGoogleItinerary(station: Station) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.google.com/maps/dir/?api=1&destination=" +
                            "${station.latitude}," +
                            "${station.longitude}"
                )
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}