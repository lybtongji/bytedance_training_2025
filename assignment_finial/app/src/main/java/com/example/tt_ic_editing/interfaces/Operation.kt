package com.example.tt_ic_editing.interfaces

interface Operation<T> {
    fun apply(target: T): T
}