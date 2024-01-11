package com.example.quetzalli.data.models

import java.io.Serializable

data class DataTraining(
    val antecedents: Long,
    val gender: Long,
    val scoreSpace: Long,
    val TimeSpace: Float,
    val scoreMem: Long,
    val TimeMem: Float,
    val scoreCal: Long,
    val TimeCal: Float
): Serializable
