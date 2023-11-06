package com.igor_shaula.hometask_zf.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.hometask_zf.R
import com.igor_shaula.hometask_zf.data.CarItemRecord

class VehicleListAdapter(private val onClickFunction: (CarItemRecord, Int) -> Unit) :
    RecyclerView.Adapter<VehicleItemViewHolder>() {

    private var items: List<CarItemRecord> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return VehicleItemViewHolder(itemView, onClickFunction)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VehicleItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun setItems(items: List<CarItemRecord>) {
        this.items = items
        notifyDataSetChanged() // todo remove this warning by providing the position somehow
    }
}
