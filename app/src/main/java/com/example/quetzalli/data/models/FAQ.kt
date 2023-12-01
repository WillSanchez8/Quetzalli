package com.example.quetzalli.data.models

import java.io.Serializable

data class FAQ(
    val id: String? = null,
    var question: String? = null,
    var answer: String? = null,
    var isExpandable : Boolean = false
):Serializable
