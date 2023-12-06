package com.example.quetzalli.data.models

import java.io.Serializable

data class Level(
    val answer: String? = null,
    val imgSequence: String? = null,
    val score: Int? = null
):Serializable

data class SequenceGraph(
    val id: String? = null,
    val levels: List<Level>? = null,
    val number_sequence: Int? = null
):Serializable

