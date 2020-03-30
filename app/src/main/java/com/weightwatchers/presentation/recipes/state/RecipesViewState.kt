package com.weightwatchers.presentation.recipes.state

import com.weightwatchers.data.model.recipe.RecipeDto
import com.ww.roxie.BaseState

data class RecipesViewState(
        val recipes: List<RecipeDto> = listOf(),
        val isIdle: Boolean = false,
        val isLoading: Boolean = false,
        val errorInfoMessage: String? = null,
        val emptyListInfoMessage : String? = null,
        val snackBarFilterInfo: String? = null
) : BaseState

