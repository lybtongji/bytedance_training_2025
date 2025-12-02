package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.google.android.material.slider.Slider

class EditLuminanceAdapter() :
    RecyclerView.Adapter<EditLuminanceAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val from: Float,
        val to: Float,
        var now: Float,
    )

    private val items = arrayOf(
        Item("亮度", -100f, 100f, 0f),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sub_slider_title)
        private val slider: Slider = itemView.findViewById(R.id.sub_slider)

        fun bind(item: Item) {
            textView.text = item.title
            slider.valueFrom = item.from
            slider.valueTo = item.to
            slider.value = item.now
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_slide, parent, false)
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