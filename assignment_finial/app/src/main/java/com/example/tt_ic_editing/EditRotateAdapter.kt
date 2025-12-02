package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EditRotateAdapter() :
    RecyclerView.Adapter<EditRotateAdapter.ViewHolder>() {
    private val params = arrayOf(
        "+90°",
        "-90°",
        "+180°",
        "↔",
        "↕",
    )

    data class Item(
        val id: Int,
        val title: String,
        val description: String?,
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        holder.bind(
            Item(
                position,
                params[position],
                null,
            )
        )
    }

    override fun getItemCount(): Int {
        return params.size
    }

}