package com.example.quetzalli.data.models

import java.io.Serializable

data class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var gender: String? = null,
    var date: String? = null,
    var occupation: String? = null
) : Serializable

