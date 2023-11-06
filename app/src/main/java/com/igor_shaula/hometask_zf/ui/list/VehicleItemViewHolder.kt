package com.igor_shaula.hometask_zf.ui.list

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.hometask_zf.R
import com.igor_shaula.hometask_zf.data.CarItemRecord

class VehicleItemViewHolder(
    itemView: View, private val onClickFunction: (CarItemRecord, Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val actvCarId: AppCompatTextView
    private val actvCarStatus: AppCompatTextView

    init {
        actvCarId = itemView.findViewById(R.id.actvCarId)
        actvCarStatus = itemView.findViewById(R.id.actvCarStatus)
    }

    fun bind(item: CarItemRecord, position: Int) {
        itemView.setOnClickListener { onClickFunction(item, position) }
        actvCarId.text = item.carId
        actvCarStatus.text = item.carStatus.name
    }
}
