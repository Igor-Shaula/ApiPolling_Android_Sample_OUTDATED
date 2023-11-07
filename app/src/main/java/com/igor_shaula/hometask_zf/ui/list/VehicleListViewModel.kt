package com.igor_shaula.hometask_zf.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.data.VehicleRecord
import com.igor_shaula.hometask_zf.data.VehiclesRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class VehicleListViewModel : ViewModel() {

    val vehiclesListMLD = MutableLiveData<List<VehicleRecord>>()

    private var repository: VehiclesRepository = VehiclesRepository()

    init {
        MainScope().launch {
            vehiclesListMLD.value = repository.readVehiclesList()
            Timber.d("vehiclesListMLD = ${vehiclesListMLD.value}")
        }
    }
}