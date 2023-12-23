package com.example.quetzalli.network

import com.example.quetzalli.network.response.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    @GET("/graph1/{user_id}/{week_number}")
    suspend fun createGraph(
        @Path("user_id") userId: String, @Path("week_number") weekNumber: String)
    : Response<ApiResponse>
}
