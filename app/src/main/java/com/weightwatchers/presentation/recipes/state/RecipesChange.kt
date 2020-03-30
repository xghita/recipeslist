package com.weightwatchers.presentation.recipes.state

import com.weightwatchers.data.network.model.Recipe

sealed class RecipesChange {
    object Loading : RecipesChange()
    object EmptyList : RecipesChange()
    data class RecipesList(val recipes: List<Recipe>) : RecipesChange()
    data class Error(val throwable: Throwable?) : RecipesChange()
    data class ShowSnackBar(val message: String?) : RecipesChange()
    object SnackBarDismissed : RecipesChange()
}