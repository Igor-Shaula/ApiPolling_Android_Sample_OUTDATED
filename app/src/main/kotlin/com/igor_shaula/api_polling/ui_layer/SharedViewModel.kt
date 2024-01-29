package com.igor_shaula.api_polling.ui_layer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.CurrentLocation
import com.igor_shaula.api_polling.data_layer.DefaultVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.data_sources.RepositoryProperty
import com.igor_shaula.api_polling.data_layer.detectVehicleStatus
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

private const val ABSENT_FAILURE_EXPLANATION_MESSAGE =
    "no failure explanation from the Repository level"

class SharedViewModel(repository: DefaultVehiclesRepository) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <SharedViewModelType : ViewModel> create(
                modelClass: Class<SharedViewModelType>,
                extras: CreationExtras
            ): SharedViewModelType {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                return SharedViewModel(
                    (application as ThisApp).getRepository()
                ) as SharedViewModelType
            }
        }
    }

    // todo: use data from vehiclesMapFlow on the UI layer
    val vehiclesMapFlow: Flow<MutableMap<String, VehicleRecord>?> = repository.vehiclesDataFlow
        .also { Timber.v("vehiclesMapFlow updated") }

    private val mldVehiclesMap = MutableLiveData<MutableMap<String, VehicleRecord>>()
    val vehiclesMap: LiveData<MutableMap<String, VehicleRecord>> get() = mldVehiclesMap

    private val mldMainErrorStateInfo = MutableLiveData<Pair<String, Boolean>>()
    val mainErrorStateInfo: LiveData<Pair<String, Boolean>> get() = mldMainErrorStateInfo

    // no need to make this LiveData private - it's only a trigger for update action
    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()
    val timeToShowGeneralBusyState = MutableLiveData<Boolean>()
    val timeToShowFakeDataProposal = MutableLiveData<Boolean>()
    val timeToAdjustForFakeData = MutableLiveData<Unit>()

    private val vehiclesMapObserver = Observer<MutableMap<String, VehicleRecord>> {
        mldVehiclesMap.value = it // all new data from the Repository comes in only in this place
        Timber.v("mldVehiclesMap.value = ${mldVehiclesMap.value}")
        if (mldVehiclesMap.value?.isEmpty() == true) processAlternativesForGettingData()
        getAllVehiclesJob?.cancel()
        getAllVehiclesJob = null
    }

    private val mainErrorStateInfoObserver = Observer<String?> {
        val pair = if (it == null) {
            Pair(ABSENT_FAILURE_EXPLANATION_MESSAGE, false)
        } else {
            Pair(it, true)
        }
        mldMainErrorStateInfo.value = pair
        Timber.v("mldMainErrorStateInfo.value: ${mldMainErrorStateInfo.value}")
    }

    private var repository: DefaultVehiclesRepository
            by RepositoryProperty(vehiclesMapObserver, mainErrorStateInfoObserver)

    private val coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)

    private var getAllVehiclesJob: Job? = null

    private var firstTimeLaunched = true

    init {
        coroutineScope.launch {
            vehiclesMapFlow.collect {
                Timber.v("vehiclesMapFlow new value = $it")
            }
        }
    }

    override fun onCleared() {
        // for some reason these observers work once during the app is closed
        repository.mainDataStorage.removeObserver(vehiclesMapObserver)
        repository.mainErrorStateInfo.removeObserver(mainErrorStateInfoObserver)
        getAllVehiclesJob?.cancel()
        coroutineScope.cancel()
        super.onCleared()
    }

    fun getAllVehiclesIds() {
        // i decided to have lazy subscription - only when we need it - here, not in init-block
        getAllVehiclesJob = coroutineScope.launch(Dispatchers.Main) { // only Main does work here
            repository.launchGetAllVehicleIdsRequest(::toggleMainBusyState)
        }
    }

    fun startGettingVehiclesDetails() {
        coroutineScope.launch {
            repository.startGettingVehiclesDetails(::updateTheViewModel, ::toggleBusyStateFor)
        }
    }

    fun stopGettingVehiclesDetails() {
        repository.stopGettingVehiclesDetails()
    }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles()

    fun getNumberOfAllVehicles() = repository.getNumberOfAllVehicles()

    private fun processAlternativesForGettingData() {
        if (firstTimeLaunched) { // during the first launch showing dialog is not needed
            firstTimeLaunched = false
            return // optimization for avoiding excess IO request to the storage
        }
        coroutineScope.launch(Dispatchers.Main) {
            timeToShowFakeDataProposal.value = true
        }
    }

    private fun updateTheViewModel(vehicleId: String, currentLocation: CurrentLocation) {
        mldVehiclesMap.value?.put(
            vehicleId, VehicleRecord(
                vehicleId, detectVehicleStatus(currentLocation), false, currentLocation
            )
        )
        // why postValue instead of setValue() -> https://www.geeksforgeeks.org/livedata-setvalue-vs-postvalue-in-android/
        timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
    }

    private fun toggleBusyStateFor(vehicleId: String, isBusy: Boolean) {
        mldVehiclesMap.value?.put(
            vehicleId,
            VehicleRecord(vehicleId, VehicleStatus.NEW_ROUND, isBusy)
        )
    }

    private fun toggleMainBusyState(isBusy: Boolean) {
        coroutineScope.launch(Dispatchers.Main) {
            timeToShowGeneralBusyState.value = isBusy
        }
    }

    fun onReadyToUseFakeData() {
        stopGettingVehiclesDetails() // to avoid any possible resource leaks if this one still works
        repository =
            ThisApp.switchActiveDataSource(ThisApp.ActiveDataSource.FAKE) // must be a new value - with fake data
        timeToAdjustForFakeData.value = Unit
    }

    fun clearPreviousFakeDataSelection() {
        firstTimeLaunched = true
        timeToShowFakeDataProposal.value = false
        mldVehiclesMap.value?.clear() // hoist up to the repository level - data must be cleared there as well
        coroutineScope.cancel()
        repository = ThisApp.switchActiveDataSource(ThisApp.ActiveDataSource.NETWORK)
    }
}
