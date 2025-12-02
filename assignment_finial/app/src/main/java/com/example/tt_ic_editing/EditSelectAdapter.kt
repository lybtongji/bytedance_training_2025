package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class EditSelectAdapter(private val subView: RecyclerView) :
    RecyclerView.Adapter<EditSelectAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val adapter: RecyclerView.Adapter<*>?,
    )

    private val items = arrayOf(
        Item("✂️", EditCutAdapter()),
        Item("🔄", EditRotateAdapter()),
        Item("🔆", EditLuminanceAdapter()),
        Item("\uD83C\uDF17", EditContrastAdapter()),
        Item("✨", EditFilterAdapter()),
        Item("\uD83D\uDE02", EditStickerAdapter()),
        Item("📝︎", null),
        Item("A", null),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textButton: Button = itemView.findViewById(R.id.main_text_btn)

        fun bind(item: Item) {
            textButton.text = item.title
            textButton.setOnClickListener { _ ->
                subView.adapter = item.adapter
            }
        }
    }

    init {
        subView.adapter = items?.first()?.adapter
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_text_button, parent, false)
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