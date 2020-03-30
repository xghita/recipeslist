package com.weightwatchers.domain.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weightwatchers.presentation.recipes.RecipesViewModel
import com.weightwatchers.presentation.recipes.state.RecipesViewState

class RecipesViewModelFactory(
        private val initialState: RecipesViewState,
        private val recipesListUseCase: RecipesListUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipesViewModel(initialState, recipesListUseCase) as T
    }
}