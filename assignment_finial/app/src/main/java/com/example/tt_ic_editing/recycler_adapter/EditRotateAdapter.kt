package com.example.tt_ic_editing.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.R

class EditRotateAdapter() :
    RecyclerView.Adapter<EditRotateAdapter.ViewHolder>() {

    data class Item(
        val title: String,
    )

    private val items = arrayOf(
        Item("+90°"),
        Item("-90°"),
        Item("+180°"),
        Item("↔"),
        Item("↕"),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: Button = itemView.findViewById(R.id.sub_text_btn)

        fun bind(item: Item) {
            textView.text = item.title
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