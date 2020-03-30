package com.weightwatchers.data.repository

import com.weightwatchers.data.network.api.ApiDecorator
import com.weightwatchers.data.network.model.Recipe
import io.reactivex.Observable

class RecipesRepository(private val apiDecorator: ApiDecorator) {

    private var recipeList: List<Recipe> = ArrayList()

    fun getRecipeList(): Observable<List<Recipe>> {

        if (recipeList.isNotEmpty()) {
            return Observable.just(recipeList)
        }

        return apiDecorator.getRecipe().doOnNext {
            recipeList = it
        }
    }

}