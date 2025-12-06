package com.example.tt_ic_editing.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.R
import com.google.android.material.slider.Slider


class EditTextEffectAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_UNKNOWN = -1
        private const val TYPE_FONT_FAMILY = 0
        private const val TYPE_FONT_SIZE = 1
        private const val TYPE_FONT_COLOR = 2
        private const val TYPE_FONT_TRANSPARENCY = 3
    }

    data class Item(
        val title: String,
    )

    private val items = arrayOf(
        Item("字体"),
        Item("字号"),
        Item("颜色"),
        Item("透明度"),
    )

    inner class FontFamilyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sub_spinner_title)
        private val spinner: Spinner = itemView.findViewById(R.id.sub_spinner)

        fun bind(item: Item) {
            textView.text = item.title
            val adapter = ArrayAdapter.createFromResource(
                itemView.context,
                R.array.edit_font_family,
                android.R.layout.simple_spinner_item,
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    inner class FontSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sub_spinner_title)
        private val spinner: Spinner = itemView.findViewById(R.id.sub_spinner)

        fun bind(item: Item) {
            textView.text = item.title
            val adapter = ArrayAdapter.createFromResource(
                itemView.context,
                R.array.edit_font_size,
                android.R.layout.simple_spinner_item,
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    inner class TransparencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sub_slider_title)
        private val slider: Slider = itemView.findViewById(R.id.sub_slider)

        fun bind(item: Item) {
            textView.text = item.title
            slider.valueFrom = 0f
            slider.valueTo = 100f
            slider.value = 100f
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textButton: Button = itemView.findViewById(R.id.sub_text_btn)

        fun bind(item: Item) {
            textButton.text = item.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_FONT_FAMILY
            1 -> TYPE_FONT_SIZE
            2 -> TYPE_FONT_COLOR
            3 -> TYPE_FONT_TRANSPARENCY
            else -> TYPE_UNKNOWN
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FONT_FAMILY -> FontFamilyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sub_text_spinner, parent, false)
            )

            TYPE_FONT_SIZE -> FontSizeViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sub_text_spinner, parent, false)
            )

            TYPE_FONT_COLOR -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sub_text_button, parent, false)
            )

            TYPE_FONT_TRANSPARENCY -> TransparencyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sub_slide, parent, false)
            )

            else -> throw IllegalStateException("未知的 viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is FontFamilyViewHolder -> holder.bind(items[position])
            is FontSizeViewHolder -> holder.bind(items[position])
            is TransparencyViewHolder -> holder.bind(items[position])
            is ViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}