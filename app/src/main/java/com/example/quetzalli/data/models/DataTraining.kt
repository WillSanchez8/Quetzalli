package com.example.quetzalli.data.models

import java.io.Serializable

data class DataTraining(
    val scoreTotal: List<Int>,
    val totalTime: List<Float>,
    val antecedents: Int,
    val gender: Int
): Serializable
