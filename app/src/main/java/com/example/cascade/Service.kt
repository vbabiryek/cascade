package com.example.cascade

import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Pokemon
}