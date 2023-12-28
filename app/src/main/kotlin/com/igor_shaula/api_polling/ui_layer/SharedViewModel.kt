package com.igor_shaula.api_polling.ui_layer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.detectVehicleStatus
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    // for inner use - only inside this ViewModel - as Google recommends in its examples
    private val mutableVehiclesMap = MutableLiveData<MutableMap<String, VehicleStatus>>()

    // for outer use - mostly in Fragments & Activities - as Google recommends in its examples
    val vehiclesMap: LiveData<MutableMap<String, VehicleStatus>> get() = mutableVehiclesMap

    private val mutableVehiclesDetailsMap =
        MutableLiveData<MutableMap<String, VehicleDetailsRecord>>()
    val vehiclesDetailsMap: LiveData<MutableMap<String, VehicleDetailsRecord>>
        get() = mutableVehiclesDetailsMap

    // no need to make this LiveData private - it's only a trigger for update action
    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()
    val timeToShowGeneralBusyState = MutableLiveData<Boolean>()
    val timeToShowStubDataProposal = MutableLiveData<Boolean>()

    private var repository: VehiclesRepository = ThisApp.getVehiclesRepository()

    private val coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)

    init {
        mutableVehiclesDetailsMap.value = mutableMapOf()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun getAllVehiclesIds() {
        coroutineScope.launch {
            timeToShowGeneralBusyState.value = true
            val vehicleIdList = repository.getAllVehiclesIds()
            mutableVehiclesMap.value = vehicleIdList
            timeToShowGeneralBusyState.value = false
            Timber.i("vehiclesMap.value = ${mutableVehiclesMap.value}")
            if (vehicleIdList.isEmpty()) processAlternativesForGettingData()
        }
    }

    fun startGettingVehiclesDetails() {
        coroutineScope.launch {
            repository.startGettingVehiclesDetails(mutableVehiclesMap.value, ::updateTheViewModel)
        }
    }

    fun stopGettingVehiclesDetails() {
        repository.stopGettingVehiclesDetails()
    }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles(mutableVehiclesMap.value)

    private fun processAlternativesForGettingData() {
        MainScope().launch(Dispatchers.IO) {
            getApplication<ThisApp>().readNeedStubDialogFromLocalPrefs()
                .collect { needToShowStubDataProposal ->
                    if (needToShowStubDataProposal) {
                        launch(Dispatchers.Main) {
                            timeToShowStubDataProposal.value = true
                        }
                    } else {
                        getApplication<ThisApp>().saveNeedStubDialogToLocalPrefs()
                    }
                }
        }
    }

    private fun updateTheViewModel(pair: Pair<String, VehicleDetailsRecord>) {
        mutableVehiclesMap.value?.put(pair.first, detectVehicleStatus(pair.second))
        mutableVehiclesDetailsMap.value?.put(pair.first, pair.second)
        mutableVehiclesDetailsMap.postValue(mutableVehiclesDetailsMap.value)
        // why postValue instead of setValue() -> https://www.geeksforgeeks.org/livedata-setvalue-vs-postvalue-in-android/
        timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
    }

    fun onReadyToUseStubData() {
        stopGettingVehiclesDetails() // to avoid any possible resource leaks if this one still works
        repository = ThisApp.getStubVehiclesRepository() // must be a new value - with stub data
    }
}
