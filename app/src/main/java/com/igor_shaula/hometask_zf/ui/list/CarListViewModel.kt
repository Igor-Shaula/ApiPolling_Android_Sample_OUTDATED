package com.igor_shaula.hometask_zf.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.data.CarItemRecord
import com.igor_shaula.hometask_zf.data.CarsRepository
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