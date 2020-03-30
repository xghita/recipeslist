package com.weightwatchers.presentation.recipes.state

import com.ww.roxie.BaseAction

sealed class RecipesAction : BaseAction {
    object LoadRecipes : RecipesAction()
    data class ShowSnackBarFilterInfo(val position: Int) : RecipesAction()
    object SnackBarFilterInfoDismissed : RecipesAction()
}