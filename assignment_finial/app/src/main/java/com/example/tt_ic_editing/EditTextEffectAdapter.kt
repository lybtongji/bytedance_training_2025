package com.example.tt_ic_editing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class EditTextEffectAdapter() :
    RecyclerView.Adapter<EditTextEffectAdapter.ViewHolder>() {

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

    //    inner class FontFamilyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val textButton: Button = itemView.findViewById(R.id.sub_text_btn)
//
//        fun bind(item: Item) {
//            textButton.text = item.title
//        }
//    }
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
    ): ViewHolder {
        return when (viewType) {
            TYPE_FONT_FAMILY -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
            )

            TYPE_FONT_SIZE -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
            )

            TYPE_FONT_COLOR -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
            )

            TYPE_FONT_TRANSPARENCY -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
            )

            else -> throw IllegalStateException("未知的 viewType: $viewType")
        }
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