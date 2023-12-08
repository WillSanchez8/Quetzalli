package com.example.quetzalli.data.models

data class Operation(
    val img: String? = null,
    val answer: Int? = null
)

data class Operations(
    val id: String? = null,
    val operations: List<Operation>? = null
)
