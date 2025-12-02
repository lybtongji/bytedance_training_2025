package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class EditFilterAdapter() :
    RecyclerView.Adapter<EditFilterAdapter.ViewHolder>() {

    data class Item(
        val title: String,
    )

    private val items = arrayOf(
        Item("原图"),
        Item("黑白"),
        Item("复古"),
        Item("清新"),
        Item("暖色调"),
        Item("冷色调"),
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textButton: Button = itemView.findViewById(R.id.sub_text_btn)

        fun bind(item: Item) {
            textButton.text = item.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_text_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}