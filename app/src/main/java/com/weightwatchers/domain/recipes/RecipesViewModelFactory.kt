package com.weightwatchers.domain.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weightwatchers.presentation.recipes.RecipesViewModel
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.utils.StaticResourcesProvider

class RecipesViewModelFactory(
        private val initialState: RecipesViewState,
        private val recipesListUseCase: RecipesListUseCase,
        private val staticResourcesProvider: StaticResourcesProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipesViewModel(initialState, recipesListUseCase, staticResourcesProvider) as T
    }
}