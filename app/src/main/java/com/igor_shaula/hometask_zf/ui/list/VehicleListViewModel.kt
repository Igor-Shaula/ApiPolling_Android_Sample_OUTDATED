package com.igor_shaula.hometask_zf.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.data.CarItemRecord
import com.igor_shaula.hometask_zf.data.VehiclesRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class VehicleListViewModel : ViewModel() {

    val carListData = MutableLiveData<List<CarItemRecord>>()

    private var repository: VehiclesRepository = VehiclesRepository()

    init {
        MainScope().launch {
            carListData.value = repository.readTheData()
            Timber.d("carListData = ${carListData.value}")
        }
    }
}