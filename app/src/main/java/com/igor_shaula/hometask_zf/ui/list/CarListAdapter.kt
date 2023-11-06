package com.igor_shaula.hometask_zf.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.hometask_zf.R
import com.igor_shaula.hometask_zf.data.CarItemRecord

class CarListAdapter(private val onClickFunction: (CarItemRecord, Int) -> Unit) :
    RecyclerView.Adapter<CarItemViewHolder>() {

    private var items: List<CarItemRecord> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarItemViewHolder(itemView, onClickFunction)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun setItems(items: List<CarItemRecord>) {
        this.items = items
        notifyDataSetChanged() // todo remove this warning by providing the position somehow
    }
}
