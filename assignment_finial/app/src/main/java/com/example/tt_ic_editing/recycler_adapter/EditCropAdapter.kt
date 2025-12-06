package com.example.tt_ic_editing.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.CropOverlayView
import com.example.tt_ic_editing.R

class EditCropAdapter(private val getRootView: (() -> View?)? = null) :
    RecyclerView.Adapter<EditCropAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val ratio: Float? = null
    )

    private val items = arrayOf(
        Item("自由"),
        Item("1:1", ratio = 1f),
        Item("4:3", ratio = 4f / 3f),
        Item("16:9", ratio = 16f / 9f),
        Item("3:4", ratio = 3f / 4f),
        Item("9:16", ratio = 9f / 16f),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textButton: Button = itemView.findViewById(R.id.sub_text_btn)

        fun bind(item: Item) {
            textButton.text = item.title
            textButton.setOnClickListener { _ ->
                getRootView?.invoke()?.run {
                    this.findViewById<CropOverlayView>(R.id.crop_overlay).ratio = item.ratio
                }
            }
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