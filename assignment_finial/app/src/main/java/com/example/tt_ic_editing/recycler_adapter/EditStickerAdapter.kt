package com.example.tt_ic_editing.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.R

class EditStickerAdapter(private val getRootView: (() -> View?)? = null) :
    RecyclerView.Adapter<EditStickerAdapter.ViewHolder>() {

    data class Item(
        val resId: Int,
    )

    private val items = arrayOf(
        Item(R.drawable.icon_stamp_100101),
        Item(R.drawable.icon_stamp_100201),
        Item(R.drawable.icon_stamp_100301),
        Item(R.drawable.icon_stamp_100401),
        Item(R.drawable.icon_stamp_100901),
        Item(R.drawable.icon_stamp_101001),
        Item(R.drawable.icon_stamp_101101),
        Item(R.drawable.icon_stamp_101201),
        Item(R.drawable.icon_stamp_101601),
        Item(R.drawable.icon_stamp_101701),
        Item(R.drawable.icon_stamp_102001),
        Item(R.drawable.icon_stamp_102101),
        Item(R.drawable.icon_stamp_102201),
        Item(R.drawable.icon_stamp_102301),
        Item(R.drawable.icon_stamp_102501),
        Item(R.drawable.icon_stamp_102701),
        Item(R.drawable.icon_stamp_102801),
        Item(R.drawable.icon_stamp_102901),
        Item(R.drawable.icon_stamp_103101),
        Item(R.drawable.icon_stamp_103401),
        Item(R.drawable.icon_stamp_103601),
        Item(R.drawable.icon_stamp_104001),
        Item(R.drawable.icon_stamp_104401),
        Item(R.drawable.icon_stamp_104501),
        Item(R.drawable.icon_stamp_104601),
        Item(R.drawable.icon_stamp_104801),
        Item(R.drawable.icon_stamp_104901),
        Item(R.drawable.icon_stamp_105001),
        Item(R.drawable.icon_stamp_105301),
        Item(R.drawable.icon_stamp_105801),
        Item(R.drawable.icon_stamp_105901),
        Item(R.drawable.icon_stamp_106001),
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.sub_image_thumb)

        fun bind(item: Item) {
            imageView.setImageResource(item.resId)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_image_thumb, parent, false)
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