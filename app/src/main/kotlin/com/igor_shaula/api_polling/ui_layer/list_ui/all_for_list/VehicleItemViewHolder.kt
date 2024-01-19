package com.igor_shaula.api_polling.ui_layer.list_ui.all_for_list

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data_layer.VehicleRecord

class VehicleItemViewHolder(
    itemView: View, private val onClickFunction: (VehicleRecord) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val actvVehicleId: AppCompatTextView
    private val actvVehicleStatus: AppCompatTextView

    init {
        actvVehicleId = itemView.findViewById(R.id.actvVehicleId)
        actvVehicleStatus = itemView.findViewById(R.id.actvVehicleStatus)
    }

    fun bind(item: VehicleRecord) {
        itemView.setOnClickListener { onClickFunction(item) }
        actvVehicleId.text = item.vehicleId
        item.vehicleStatus.apply {
            // i decided to skip resetting of the VehicleStatus on the start of every polling tick
//            if (this == VehicleStatus.NEW_ROUND) {
//                actvVehicleStatus.setTextColor(Color.LTGRAY)
//                return@apply
//            }
            actvVehicleStatus.text = this.uiStatus
            if (this.isNear == null) {
                actvVehicleStatus.setTextColor(Color.DKGRAY)
            } else if (this.isNear) {
                actvVehicleStatus.setTextColor(Color.BLUE)
            } else {
                actvVehicleStatus.setTextColor(Color.RED)
            }
        }
    }
}
