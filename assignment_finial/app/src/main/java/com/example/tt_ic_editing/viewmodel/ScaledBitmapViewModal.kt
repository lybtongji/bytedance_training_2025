package com.example.tt_ic_editing.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.tt_ic_editing.ScaledBitmap

class ScaledBitmapViewModal : ViewModel() {
    var uri: Uri? = null
    val bitmap = ScaledBitmap()
    var opIndex = 0
}
