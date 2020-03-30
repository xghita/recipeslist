package com.weightwatchers.data.network.model.recipe

import com.weightwatchers.ww_exercise_01.BuildConfig
import okhttp3.internal.toImmutableList

object RecipesMapper {

    fun mapToRecipeDtoList(recipes: List<Recipe>): List<RecipeDto> {
        val recipesDto: MutableList<RecipeDto> = ArrayList()
        for (recipe in recipes) {
            recipesDto.add(RecipeDto(
                    title = recipe.title,
                    photoUrl = "${BuildConfig.SERVER_URL}$recipe.image",
                    filter = recipe.filter.substringAfter(":").substringBefore("\\")))
        }

        return recipesDto.toImmutableList()
    }
}