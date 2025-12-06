package com.example.tt_ic_editing.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tt_ic_editing.operations.OperationSequence

class OperationViewModel<T> : ViewModel() {
    val sequence = OperationSequence<T>()
}
