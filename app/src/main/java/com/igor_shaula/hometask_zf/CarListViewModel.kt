package com.igor_shaula.hometask_zf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CarListViewModel : ViewModel() {

    val carListData = MutableLiveData<List<CarItemRecord>>()

    private var repository: CarsRepository = CarsRepository()

    init {
        carListData.value = repository.readTheData()
    }
}