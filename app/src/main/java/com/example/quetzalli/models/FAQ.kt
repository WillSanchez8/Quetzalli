package com.example.quetzalli.models

import java.io.Serializable

data class FAQ(
    val id: String,
    var question: String,
    var answer: String,
    var isExpandable : Boolean = false
):Serializable
