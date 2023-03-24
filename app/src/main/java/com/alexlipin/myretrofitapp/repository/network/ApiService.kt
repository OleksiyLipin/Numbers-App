package com.alexlipin.myretrofitapp.repository.network

import com.alexlipin.myretrofitapp.model.FactItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{number}/trivia?json")
    suspend fun getFact(@Path("number") number: Int): Response<FactItem>

    @GET("random/trivia?json")
    suspend fun getRandomFact(): Response<FactItem>
}