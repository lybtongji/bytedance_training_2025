package com.example.tt_ic_editing

import androidx.lifecycle.ViewModel

class OperationViewModel<T> : ViewModel() {
    val sequence = OperationSequence<T>()
}
