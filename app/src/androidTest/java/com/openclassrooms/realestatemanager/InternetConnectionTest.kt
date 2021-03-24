package com.openclassrooms.realestatemanager

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class InternetConnectionTest {

    @Test
    fun checkInternetConnection() {
        assertEquals(true, Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().getTargetContext()))
    }
}