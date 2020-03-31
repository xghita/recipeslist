package com.weightwatchers.presentation.recipes

import com.weightwatchers.domain.recipes.RecipesUseCase
import com.weightwatchers.presentation.recipes.state.RecipesChange
import com.weightwatchers.presentation.recipes.state.RecipesAction
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.ww_exercise_01.R
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RecipesViewModel(override val initialState: RecipesViewState, private val recipesUseCase: RecipesUseCase) : BaseViewModel<RecipesAction, RecipesViewState>() {

    private val reducer: Reducer<RecipesViewState, RecipesChange> = { state, change ->
        when (change) {
            is RecipesChange.Loading -> {
                state.copy(
                        isLoading = true,
                        recipes = emptyList(),
                        errorInfoMessage = null,
                        emptyListInfoMessage = null
                )
            }
            is RecipesChange.EmptyList -> {
                state.copy(
                        isLoading = false,
                        recipes = emptyList(),
                        errorInfoMessage = null,
                        emptyListInfoMessage = R.string.recipes_empty_list
                )
            }
            is RecipesChange.RecipesList -> state.copy(
                    isLoading = false,
                    recipes = change.recipes
            )
            is RecipesChange.Error -> state.copy(
                    isLoading = false,
                    errorInfoMessage = R.string.generic_network_error
            )
            is RecipesChange.ShowSnackBarFilterInfo -> state.copy(
                    snackBarFilterInfo = change.message
            )
            is RecipesChange.SnackBarFilterInfoDismissed -> state.copy(
                    snackBarFilterInfo = null
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadRecipes = actions.ofType<RecipesAction.LoadRecipes>()
                .switchMap {
                    recipesUseCase.loadRecipes()
                            .subscribeOn(Schedulers.io())
                            .map {
                                if (it.isNotEmpty()) {
                                    RecipesChange.RecipesList(it)
                                } else {
                                    RecipesChange.EmptyList
                                }
                            }
                            .defaultIfEmpty(RecipesChange.EmptyList)
                            .onErrorReturn {
                                RecipesChange.Error(it)
                            }
                            .startWith(RecipesChange.Loading)
                }

        val showSnackBar = actions.ofType<RecipesAction.ShowSnackBarFilterInfo>()
                .switchMap { action ->
                    recipesUseCase.getRecipeFilter(action.position)
                            .subscribeOn(Schedulers.io())
                            .map<RecipesChange> {
                                RecipesChange.ShowSnackBarFilterInfo(it)
                            }
                            .onErrorReturn {
                                RecipesChange.Error(it)
                            }
                }

        val snackBarDismissed = actions.ofType<RecipesAction.SnackBarFilterInfoDismissed>()
                .switchMap {
                    Observable.just(RecipesChange.SnackBarFilterInfoDismissed)
                }


        disposables += Observable.merge(loadRecipes, showSnackBar, snackBarDismissed)
                .scan(initialState, reducer)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state::setValue, Timber::e)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

}