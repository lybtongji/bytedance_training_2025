package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class EditCutAdapter() :
    RecyclerView.Adapter<EditCutAdapter.ViewHolder>() {
    private val params = arrayOf(
        "□",
        "1:1",
        "4:3",
        "16:9",
        "3:4",
        "9:16",
    )

    data class Item(
        val id: Int,
        val title: String,
        val description: String?,
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