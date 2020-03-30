package com.weightwatchers.presentation.recipes.state

import com.weightwatchers.data.network.model.Recipe
import com.weightwatchers.data.network.model.RecipeDto
import com.ww.roxie.BaseState

data class RecipesViewState(
        val recipes: List<RecipeDto> = listOf(),
        val isIdle: Boolean = false,
        val isLoading: Boolean = false,
        val errorInfoMessage: String? = null,
        val emptyListInfoMessage : String? = null,
        val snackBarMessage: String? = null
) : BaseState

