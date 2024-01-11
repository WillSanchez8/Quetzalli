package com.example.quetzalli.network.response

import com.google.gson.annotations.SerializedName

data class ApiResponse (
    @SerializedName("url")
    val url : String?,
    @SerializedName("message")
    val message : String?,
)