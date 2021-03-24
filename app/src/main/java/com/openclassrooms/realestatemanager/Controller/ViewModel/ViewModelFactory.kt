package com.openclassrooms.realestatemanager.Controller.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import io.reactivex.Scheduler


class ViewModelFactory(private val estateDataRepository: EstateDataRepository,
                       private val imageDataRepository: ImageDataRepository,
                       private val locationDataRepository: LocationDataRepository,
                       private val subscriberOn: Scheduler,
                       private val observerOn: Scheduler) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstateViewModel(estateDataRepository,imageDataRepository,locationDataRepository,subscriberOn,observerOn) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}