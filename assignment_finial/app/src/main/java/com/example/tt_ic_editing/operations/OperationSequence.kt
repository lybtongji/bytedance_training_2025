package com.example.tt_ic_editing.operations

import android.graphics.Bitmap
import android.util.Log
import com.example.tt_ic_editing.interfaces.Operation

class OperationSequence<T> {
    private val operations = mutableListOf<Operation<T>>()

    fun add(op: Operation<T>): OperationSequence<T> {
        operations.add(op)
        return this
    }

    fun remove(): OperationSequence<T> {
        operations.removeLastOrNull()
        return this
    }

    fun execute(target: T, from: Int = 0): T {
        var result = target
        for (op in operations.subList(from, operations.size)) {
            result = op.apply(result)
        }
        return result
    }

    fun execute_last(target: T): T {
        return operations.lastOrNull()?.apply(target) ?: target
    }
}