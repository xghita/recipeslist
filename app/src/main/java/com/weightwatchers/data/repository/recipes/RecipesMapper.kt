package com.weightwatchers.data.repository.recipes

import com.weightwatchers.data.model.recipe.Recipe
import com.weightwatchers.data.model.recipe.RecipeDto
import com.weightwatchers.data.repository.Mapper
import com.weightwatchers.ww_exercise_01.BuildConfig

class RecipesMapper : Mapper<Recipe, RecipeDto> {

    override fun mapToDto(recipe: Recipe): RecipeDto {
        return RecipeDto(
                title = recipe.title,
                photoUrl = "${BuildConfig.SERVER_URL}${recipe.image}",
                filter = recipe.filter.substringAfter(":").substringBefore("\\"))
    }

    override fun mapToDtoList(recipes: List<Recipe>): List<RecipeDto> {
        return recipes.map {
            mapToDto(it)
        }
    }

}
