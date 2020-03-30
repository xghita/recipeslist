package com.weightwatchers.domain.recipes

import com.weightwatchers.data.network.model.recipe.RecipeDto
import com.weightwatchers.data.repository.RecipesRepository
import io.reactivex.Observable

class RecipesListUseCase(private val recipesRepository: RecipesRepository) {

    fun loadRecipes(): Observable<List<RecipeDto>> = recipesRepository.getRecipeList()

    fun getRecipeFilter(position: Int): Observable<String> {
        return recipesRepository.getRecipeList().flatMap {
            Observable.just(it[position].filter)
        }
    }
}