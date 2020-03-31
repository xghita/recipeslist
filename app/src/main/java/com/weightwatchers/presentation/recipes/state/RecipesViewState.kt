package com.weightwatchers.presentation.recipes.state

import androidx.annotation.StringRes
import com.weightwatchers.data.model.recipe.RecipeDto
import com.ww.roxie.BaseState

data class RecipesViewState(
        val recipes: List<RecipeDto> = listOf(),
        val isLoading: Boolean = false,
        @StringRes
        val errorInfoMessage: Int? = null,
        @StringRes
        val emptyListInfoMessage: Int? = null,
        val snackBarFilterInfo: String? = null
) : BaseState

