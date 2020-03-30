package com.weightwatchers.presentation.recipes

import com.weightwatchers.domain.recipes.RecipesListUseCase
import com.weightwatchers.presentation.recipes.state.RecipesChange
import com.weightwatchers.presentation.recipes.state.RecipesAction
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.utils.StaticResourcesProvider
import com.weightwatchers.ww_exercise_01.R
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RecipesViewModel(override val initialState: RecipesViewState, private val recipesListUseCase: RecipesListUseCase) : BaseViewModel<RecipesAction, RecipesViewState>() {

    private val reducer: Reducer<RecipesViewState, RecipesChange> = { state, change ->
        when (change) {
            is RecipesChange.Loading -> {
                state.copy(
                        isIdle = false,
                        isLoading = true,
                        recipes = emptyList(),
                        errorInfoMessage = null,
                        emptyListInfoMessage = null
                )
            }
            is RecipesChange.EmptyList -> {
                state.copy(
                        isIdle = false,
                        isLoading = false,
                        recipes = emptyList(),
                        errorInfoMessage = null,
                        emptyListInfoMessage = StaticResourcesProvider.getStaticStringResource(R.string.recipes_empty_list)
                )
            }
            is RecipesChange.RecipesList -> state.copy(
                    isLoading = false,
                    recipes = change.recipes
            )
            is RecipesChange.Error -> state.copy(
                    isLoading = false,
                    errorInfoMessage = StaticResourcesProvider.getStaticStringResource(R.string.generic_network_error)
            )
            is RecipesChange.ShowSnackBar -> state.copy(
                    snackBarMessage = change.message
            )
            is RecipesChange.SnackBarDismissed -> state.copy(
                    snackBarMessage = null
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadRecipes = actions.ofType<RecipesAction.LoadRecipes>()
                .switchMap {
                    recipesListUseCase.loadRecipes()
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


        val showSnackBar = actions.ofType<RecipesAction.ShowSnackBarWithFilterInfo>()
                .switchMap { action ->
                    recipesListUseCase.getRecipeFilter(action.position)
                            .subscribeOn(Schedulers.io())
                            .map<RecipesChange> {
                                if (it.isNotEmpty()) {
                                    RecipesChange.ShowSnackBar(it)
                                } else {
                                    RecipesChange.ShowSnackBar(StaticResourcesProvider.getStaticStringResource(R.string.recipes_filter_info))
                                }
                            }
                            .onErrorReturn {
                                RecipesChange.Error(it)
                            }
                }

        val snackBarDismissed = actions.ofType<RecipesAction.SnackBarFilterInfoDismissed>()
                .switchMap {
                    Observable.just(RecipesChange.SnackBarDismissed)
                }


        disposables += Observable.merge(loadRecipes, showSnackBar, snackBarDismissed)
                .scan(initialState, reducer)
                .filter {
                    !it.isIdle
                }
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