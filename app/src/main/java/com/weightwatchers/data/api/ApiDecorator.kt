package com.weightwatchers.data.api

import com.weightwatchers.data.model.recipe.Recipe
import io.reactivex.Observable
import retrofit2.Retrofit

class ApiDecorator(retrofit: Retrofit) {

    private val apiCall: Api = retrofit.create(Api::class.java)

    fun getRecipe(): Observable<List<Recipe>> {
        return apiCall.getRecipes()
    }

}