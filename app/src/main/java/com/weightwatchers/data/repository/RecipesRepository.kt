package com.weightwatchers.data.repository

import com.weightwatchers.data.network.api.ApiDecorator
import com.weightwatchers.data.network.model.Recipe
import com.weightwatchers.data.network.model.RecipeDto
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
                    recipesDtoList = mapToRecipeDtoList(it)
                    Observable.just(recipesDtoList)
                }
    }


    private fun mapToRecipeDtoList(recipes: List<Recipe>): List<RecipeDto> {

        val recipesDto: MutableList<RecipeDto> = ArrayList()
        for (recipe in recipes) {
            recipesDto.add(RecipeDto(title = recipe.title, photoUrl = "${BuildConfig.SERVER_URL}$recipe.image",
                    filter = recipe.filter.substringAfter(":").substringBefore("\\")))
        }

        return recipesDto.toImmutableList()
    }

}