package com.igor_shaula.api_polling.ui.list

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data.VehicleRecord

class VehicleItemViewHolder(
    itemView: View, private val onClickFunction: (VehicleRecord, Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val actvVehicleId: AppCompatTextView
    private val actvVehicleStatus: AppCompatTextView

    init {
        actvVehicleId = itemView.findViewById(R.id.actvVehicleId)
        actvVehicleStatus = itemView.findViewById(R.id.actvVehicleStatus)
    }

    fun bind(item: VehicleRecord, position: Int) {
        itemView.setOnClickListener { onClickFunction(item, position) }
        actvVehicleId.text = item.vehicleId
        actvVehicleStatus.text = item.vehicleStatus.name
    }
}
