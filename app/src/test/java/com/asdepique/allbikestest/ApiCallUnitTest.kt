package com.asdepique.allbikestest

import com.asdepique.allbikestest.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiCallUnitTest {
    private val disposables = CompositeDisposable()

    @Test
    fun testStationsCall() {
        disposables.add(
            StationRepository.createAllStationsCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { apiResponse -> assert(apiResponse?.isNotEmpty() == true) },
                    { throwable -> assert(false) }
                ))
    }
}