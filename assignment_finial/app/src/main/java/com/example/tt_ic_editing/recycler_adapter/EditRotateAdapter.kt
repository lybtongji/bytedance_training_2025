package com.example.tt_ic_editing.recycler_adapter

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.R

class EditRotateAdapter(private val doRotate: ((((Bitmap) -> Matrix)) -> Unit)? = null) :
    RecyclerView.Adapter<EditRotateAdapter.ViewHolder>() {

    data class Item(
        val title: String,
        val getMatrix: ((Bitmap) -> Matrix)
    )

    private val items = arrayOf(
        Item("+90°", ::rotate90),
        Item("-90°", ::rotateNeg90),
        Item("+180°", ::rotate180),
        Item("↔", ::flip_h),
        Item("↕", ::flip_v),
    )

    fun rotate90(bitmap: Bitmap): Matrix {
        return Matrix().apply {
            postTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
            postRotate(90f)
            postTranslate(bitmap.height / 2f, bitmap.width / 2f)
        }
    }

    fun rotateNeg90(bitmap: Bitmap): Matrix {
        return Matrix().apply {
            postTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
            postRotate(-90f)
            postTranslate(bitmap.height / 2f, bitmap.width / 2f)
        }
    }

    fun rotate180(bitmap: Bitmap): Matrix {
        return Matrix().apply {
            postRotate(180f, bitmap.width / 2f, bitmap.height / 2f)
        }
    }

    fun flip_h(bitmap: Bitmap): Matrix {
        return Matrix().apply {
            preScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
    }

    fun flip_v(bitmap: Bitmap): Matrix {
        return Matrix().apply {
            preScale(1f, -1f, bitmap.width / 2f, bitmap.height / 2f)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: Button = itemView.findViewById(R.id.sub_text_btn)

        fun bind(item: Item) {
            textView.text = item.title
            textView.setOnClickListener { _ ->
                doRotate?.invoke(item.getMatrix)
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