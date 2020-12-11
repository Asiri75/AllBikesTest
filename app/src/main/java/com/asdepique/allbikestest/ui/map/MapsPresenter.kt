package com.asdepique.allbikestest.ui.map

import android.util.Log
import com.asdepique.allbikestest.model.Station
import com.asdepique.allbikestest.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapsPresenter(private val contract: MapsContract) {

    private val disposables = CompositeDisposable()

    fun getAllStations(){
        disposables.add(
            StationRepository.createAllStationsCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { apiResponse -> onStationsCallSuccess(apiResponse) },
                    { throwable -> onStationsCallError(throwable) }
                ))
    }

    private fun onStationsCallSuccess(stations: List<Station>?) {
        stations?.let {
            Log.d("MapsPresenter", "Got ${it.count()} stations")
            contract.onStationsResult(it)
        }?: contract.onStationsRequestError()
    }

    private fun onStationsCallError(throwable: Throwable) {
        Log.e("MapsPresenter", "Error during call", throwable)
        contract.onStationsRequestError()
    }

}