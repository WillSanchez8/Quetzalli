package com.example.quetzalli.data.models

import com.google.firebase.Timestamp
import java.io.Serializable

data class Test(
    val userId: String? = null,
    val scoreTotal: Int? = null,
    val totalTime: String? = null,
    val date: Timestamp? = null,
    var completed: Boolean? = null
): Serializable


