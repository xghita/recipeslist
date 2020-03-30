package com.weightwatchers.presentation.recipes.state

import com.weightwatchers.data.network.model.recipe.RecipeDto

sealed class RecipesChange {
    object Loading : RecipesChange()
    object EmptyList : RecipesChange()
    data class RecipesList(val recipes: List<RecipeDto>) : RecipesChange()
    data class Error(val throwable: Throwable?) : RecipesChange()
    data class ShowSnackBar(val message: String?) : RecipesChange()
    object SnackBarDismissed : RecipesChange()
}