package com.weightwatchers.presentation.recipes.state

import com.weightwatchers.data.model.recipe.RecipeDto

sealed class RecipesChange {
    object Loading : RecipesChange()
    object EmptyList : RecipesChange()
    data class RecipesList(val recipes: List<RecipeDto>) : RecipesChange()
    data class Error(val throwable: Throwable?) : RecipesChange()
    data class ShowSnackBarFilterInfo(val message: String?) : RecipesChange()
    object SnackBarFilterInfoDismissed : RecipesChange()
}