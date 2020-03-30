package com.weightwatchers.presentation.recipes.state

import com.ww.roxie.BaseAction

sealed class RecipesAction : BaseAction {
    object LoadRecipes : RecipesAction()
    data class ShowSnackBarWithFilterInfo(val position: Int) : RecipesAction()
    object SnackBarFilterInfoDismissed : RecipesAction()
}