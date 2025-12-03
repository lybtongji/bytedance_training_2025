package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class EditCropAdapter() :
    RecyclerView.Adapter<EditCropAdapter.ViewHolder>() {

    data class Item(
        val title: String,
    )

    private val items = arrayOf(
        Item("□"),
        Item("1:1"),
        Item("4:3"),
        Item("16:9"),
        Item("3:4"),
        Item("9:16"),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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