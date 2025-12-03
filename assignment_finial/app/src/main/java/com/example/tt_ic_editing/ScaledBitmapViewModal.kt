package com.example.tt_ic_editing

import android.net.Uri
import androidx.lifecycle.ViewModel

class ScaledBitmapViewModal : ViewModel() {
    var uri: Uri? = null
    val bitmap = ScaledBitmap()
}
