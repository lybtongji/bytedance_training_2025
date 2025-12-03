package com.example.tt_ic_editing

enum class OperationType {
    ROTATE,
    CROP,
    FILTER,
}

data class ImageOperation(
    val type: OperationType,
    val params: Map<String, Any>
)