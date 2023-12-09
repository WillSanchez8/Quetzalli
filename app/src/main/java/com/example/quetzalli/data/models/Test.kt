package com.example.quetzalli.data.models

import java.io.Serializable

data class Test(
    val userId: String,
    val scoreTotal: Int,
    val totalTime: String,
    val date: String
): Serializable

