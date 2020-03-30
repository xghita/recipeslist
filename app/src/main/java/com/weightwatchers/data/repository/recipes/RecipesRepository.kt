package com.weightwatchers.data.repository.recipes

import com.weightwatchers.data.api.ApiDecorator
import com.weightwatchers.data.model.recipe.RecipeDto
import io.reactivex.Observable

class RecipesRepository(private val apiDecorator: ApiDecorator, private val mapper: RecipesMapper) {

    private var recipesDtoList: List<RecipeDto> = ArrayList()

    fun getRecipeList(): Observable<List<RecipeDto>> {

        if (recipesDtoList.isNotEmpty()) {
            return Observable.just(recipesDtoList)
        }

        return apiDecorator.getRecipe()
                .flatMap {
                    recipesDtoList = mapper.mapToDtoList(it)
                    Observable.just(recipesDtoList)
                }
    }
}