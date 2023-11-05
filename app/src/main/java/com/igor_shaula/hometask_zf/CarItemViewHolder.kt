package com.igor_shaula.hometask_zf

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class CarItemViewHolder(
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
