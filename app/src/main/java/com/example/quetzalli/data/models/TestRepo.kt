package com.example.quetzalli.data.models

import java.io.Serializable

data class TestRep(
    val id: String? = null,
    val name: String? = null,
    val time : String? = null,
    val description : String? = null,
    val image : String? = null,
    val type: String? = null
): Serializable

