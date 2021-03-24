package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import com.openclassrooms.realestatemanager.utils.GeocodeStream
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class StreamTest {

    private val apiKey = BuildConfig.google_maps_api_key
    private val address = "960 Fifth Avenue New york, USA 10021"


    @Test
    fun checkFetchGeocodeInfo(){
        val geocodeInfoObservable = GeocodeStream().streamFetchGeocodeInfo(address,apiKey)
        val testObserver = TestObserver<GeocodeInfo>()
        geocodeInfoObservable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent()
        val geocodeInfo = testObserver.values()[0]
        assertEquals("OK", geocodeInfo.status)
    }
}