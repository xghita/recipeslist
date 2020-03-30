package com.weightwatchers.data.repository

import com.weightwatchers.data.network.api.ApiDecorator
import com.weightwatchers.data.network.model.recipe.Recipe
import com.weightwatchers.data.network.model.recipe.RecipeDto
import com.weightwatchers.data.network.model.recipe.RecipesMapper
import com.weightwatchers.ww_exercise_01.BuildConfig
import io.reactivex.Observable
import okhttp3.internal.toImmutableList

class RecipesRepository(private val apiDecorator: ApiDecorator) {

    private var recipesDtoList: List<RecipeDto> = ArrayList()

    fun getRecipeList(): Observable<List<RecipeDto>> {

        if (recipesDtoList.isNotEmpty()) {
            return Observable.just(recipesDtoList)
        }

        return apiDecorator.getRecipe()
                .flatMap {
                    recipesDtoList = RecipesMapper.mapToRecipeDtoList(it)
                    Observable.just(recipesDtoList)
                }
    }

}