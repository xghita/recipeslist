package com.weightwatchers.data.network.api

import com.weightwatchers.data.network.model.Recipe
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface Api {

    @Headers("ContentType: application/json")
    @GET("/assets/cmx/us/messages/collections.json")
    fun getRecipes(): Observable<List<Recipe>>
}