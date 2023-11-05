package com.igor_shaula.hometask_zf

data class CarItemRecord(
    val carId: String,
    val carStatus: CarStatus
)

enum class CarStatus {
    CLOSE, FAR_AWAY
}
