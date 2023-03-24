package com.alexlipin.myretrofitapp.repository.network

import com.alexlipin.myretrofitapp.model.FactItem
import retrofit2.Response

class Repository {

    suspend fun getNumberFact(number: Int): Response<FactItem> {
        return RetrofitInstance.api.getFact(number)
    }

    suspend fun getRandomNumberFact(): Response<FactItem> {
        return RetrofitInstance.api.getRandomFact()
    }
}