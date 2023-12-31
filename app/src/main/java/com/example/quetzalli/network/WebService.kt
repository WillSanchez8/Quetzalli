package com.example.quetzalli.network

import com.example.quetzalli.network.response.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    @GET("/graph1/{user_id}")
    suspend fun createGraph1(@Path("user_id") userId: String): Response<ApiResponse>

    @GET("/graph2/{user_id}")
    suspend fun createGraph2(@Path("user_id") userId: String): Response<ApiResponse>

    @GET("/graph3/{user_id}")
    suspend fun createGraph3(@Path("user_id") userId: String): Response<ApiResponse>
}
