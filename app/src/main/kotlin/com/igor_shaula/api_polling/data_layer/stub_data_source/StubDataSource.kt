package com.igor_shaula.api_polling.data_layer.stub_data_source

import com.igor_shaula.api_polling.data_layer.AbstractVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords

class StubDataSource : AbstractVehiclesRepository() {

    private val stubDataSourceDelegate = StubDataSourceDelegate()

    override suspend fun readVehiclesList(): List<VehicleRecord> =
        stubDataSourceDelegate.getVehiclesList().toVehicleItemRecords()

    override suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord =
        stubDataSourceDelegate.getVehicleDetails(vehicleId).toVehicleItemRecords()
}
