package com.weightwatchers.ww_exercise_01

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.weightwatchers.data.model.recipe.RecipeDto
import com.weightwatchers.domain.recipes.RecipesUseCase
import com.weightwatchers.presentation.recipes.RecipesViewModel
import com.weightwatchers.presentation.recipes.state.RecipesAction
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private val recipesList = listOf(RecipeDto("filter0", "image_url0", "title0"),
            RecipeDto("filter1", "image_url1", "title1"))

    private lateinit var recipesViewModel: RecipesViewModel
    private val testObserver = mock<Observer<RecipesViewState>>()
    private val recipesUseCase = mock<RecipesUseCase>()

    @Before
    fun setUp() {
        recipesViewModel = RecipesViewModel(RecipesViewState(), recipesUseCase)
        recipesViewModel.observableState.observeForever(testObserver)
        verify(testObserver).onChanged(RecipesViewState())
    }

    @Test
    fun verifyLoadStateSuccess() {

        val recipesLoading = RecipesViewState(isLoading = true)
        val recipesLoaded = RecipesViewState(recipesList)

        whenever(recipesUseCase.loadRecipes()).thenReturn(Observable.just(recipesList))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(recipesLoaded)
        }

        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyLoadEmptyState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val emptyResult = RecipesViewState(emptyListInfoMessage = R.string.recipes_empty_list)

        whenever(recipesUseCase.loadRecipes()).thenReturn(Observable.just(emptyList()))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(emptyResult)
        }

        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyLoadErrorState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val errorResult = RecipesViewState(errorInfoMessage = R.string.generic_network_error)

        whenever(recipesUseCase.loadRecipes()).thenReturn(Observable.error(RuntimeException()))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(errorResult)
        }

        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyShowAndDismissFilterInfo() {

        val position = 0

        whenever(recipesUseCase.getRecipeFilter(position)).thenReturn(Observable.just(recipesList[position].filter))

        recipesViewModel.dispatch(RecipesAction.ShowSnackBarFilterInfo(position))
        testSchedulerRule.triggerActions()

        verify(recipesUseCase).getRecipeFilter(position)

        verify(testObserver).onChanged(RecipesViewState(snackBarFilterInfo = recipesList[position].filter))
        verify(testObserver).onChanged(RecipesViewState())

        verifyNoMoreInteractions(testObserver)
    }

}