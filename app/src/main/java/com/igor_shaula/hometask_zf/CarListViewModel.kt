package com.igor_shaula.hometask_zf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class CarListViewModel : ViewModel() {

    val carListData = MutableLiveData<List<CarItemRecord>>()

    private var repository: CarsRepository = CarsRepository()

    init {
        MainScope().launch {
            carListData.value = repository.readTheData()
            Timber.d("carListData = ${carListData.value}")
        }
    }
}