package com.example.tt_ic_editing.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.PreviewImageView
import com.example.tt_ic_editing.R
import com.google.android.material.slider.Slider

class EditContrastAdapter(private val getRootView: (() -> View?)? = null) :
    RecyclerView.Adapter<EditContrastAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val from: Float,
        val to: Float,
        var now: Float,
    )

    private val items = arrayOf(
        Item("对比度", -50f, 150f, 0f),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sub_slider_title)
        private val slider: Slider = itemView.findViewById(R.id.sub_slider)

        fun bind(item: Item) {
            textView.text = item.title
            slider.valueFrom = item.from
            slider.valueTo = item.to
            slider.value = item.now
            slider.addOnChangeListener { slider, value, fromUser ->
                val view = getRootView?.invoke()
                view?.run {
                    val previewImageView = this.findViewById<PreviewImageView>(R.id.edit_image)
                    previewImageView.updateTone(
                        contrast = value,
                    )
                }
            }
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