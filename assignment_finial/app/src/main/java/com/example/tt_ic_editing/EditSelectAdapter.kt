package com.example.tt_ic_editing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.interfaces.OnEditSelectedListener

class EditSelectAdapter() :
    RecyclerView.Adapter<EditSelectAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val adapter: RecyclerView.Adapter<*>?,
        var isSelected: Boolean = false,
    )

    private val items = arrayOf(
        Item("✂️", EditCropAdapter()),
        Item("🔄", EditRotateAdapter()),
        Item("🔆", EditLuminanceAdapter()),
        Item("\uD83C\uDF17", EditContrastAdapter()),
        Item("✨", EditFilterAdapter()),
        Item("\uD83D\uDE02", EditStickerAdapter()),
        Item("📝︎", EditTextEffectAdapter()),
        Item("A", null),
    )

//    init {
//        subView.adapter = items?.first()?.adapter
//    }

    var onEditSelectedListener: OnEditSelectedListener? = null

    var selectedPosition: Int = 0
        get() = field
        set(value) {
            Log.d("App", "value: $value, size: ${items.size}")
            if (value in 0..<items.size) {
//                subView.adapter = items[value].adapter
                field = value
                items.forEach { it.isSelected = false }
                items[value].isSelected = true
                onEditSelectedListener?.onItemSelected(value)
            }
        }

    val adapter: RecyclerView.Adapter<*>?
        get() = items[selectedPosition].adapter

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textButton: Button = itemView.findViewById(R.id.main_text_btn)

        fun bind(item: Item, position: Int) {
            textButton.text = item.title
//            textButton.isSelected = false
            textButton.setOnClickListener { _ ->
//                subView.adapter = item.adapter
                selectedPosition = position
            }
        }
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
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}