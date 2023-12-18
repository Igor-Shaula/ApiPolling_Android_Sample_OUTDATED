package com.igor_shaula.api_polling.ui_layer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.detectVehicleStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class SharedViewModel : ViewModel() {

    // for inner use - only inside this ViewModel - as Google recommends in its examples
    private val mutableVehiclesMap = MutableLiveData<MutableMap<String, VehicleStatus>>()

    // for outer use - mostly in Fragments & Activities - as Google recommends in its examples
    val vehiclesMap: MutableLiveData<MutableMap<String, VehicleStatus>> get() = mutableVehiclesMap

    val vehiclesDetailsMap by lazy { MutableLiveData<MutableMap<String, VehicleDetailsRecord>>() }

    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()

    private var repository: TheRepository = ThisApp.getVehiclesRepository()

    fun getAllVehiclesIds() {
        MainScope().launch {
            mutableVehiclesMap.value = repository.getAllVehiclesIds()
            Timber.i("vehiclesMap.value = ${mutableVehiclesMap.value}")
        }
    }

    fun startGettingVehiclesDetails() {
        MainScope().launch {
            repository.startGettingVehiclesDetails(mutableVehiclesMap.value, ::updateTheViewModel)
        }
    }

    fun stopGettingVehiclesDetails() {
        repository.stopGettingVehiclesDetails()
    }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles(mutableVehiclesMap.value)

    private fun updateTheViewModel(pair: Pair<String, VehicleDetailsRecord>) {
        mutableVehiclesMap.value?.put(pair.first, detectVehicleStatus(pair.second))
        vehiclesDetailsMap.value?.put(pair.first, pair.second)
        timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
    }
}
