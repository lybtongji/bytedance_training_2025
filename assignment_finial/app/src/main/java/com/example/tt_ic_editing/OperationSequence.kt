package com.example.tt_ic_editing

class OperationSequence<T> {
    private val operations = mutableListOf<Operation<T>>()

    fun add(op: Operation<T>): OperationSequence<T> {
        operations.add(op)
        return this // 支持链式
    }

    fun execute(target: T): T {
        var result = target
        for (op in operations) {
            result = op.apply(result)
        }
        return result
    }
}
