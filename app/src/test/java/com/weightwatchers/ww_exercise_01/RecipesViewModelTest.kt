package com.weightwatchers.ww_exercise_01

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.weightwatchers.data.model.recipe.RecipeDto
import com.weightwatchers.domain.recipes.RecipesListUseCase
import com.weightwatchers.presentation.recipes.RecipesViewModel
import com.weightwatchers.presentation.recipes.state.RecipesAction
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.utils.StaticResourcesProvider
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
    private val recipesListUseCase = mock<RecipesListUseCase>()
    private val staticResourcesProvider = mock<StaticResourcesProvider>()

    @Before
    fun setUp() {
        recipesViewModel = RecipesViewModel(RecipesViewState(), recipesListUseCase, staticResourcesProvider)
        recipesViewModel.observableState.observeForever(testObserver)
    }

    @Test
    fun verifyLoadStateSuccess() {

        val recipesLoading = RecipesViewState(isLoading = true)
        val recipesLoaded = RecipesViewState(recipesList)

        whenever(recipesListUseCase.loadRecipes()).thenReturn(Observable.just(recipesList))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(recipesLoaded)
        }
    }

    @Test
    fun verifyLoadEmptyState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val emptyResultMessage = "There are no recipes available at the moment."
        val emptyResult = RecipesViewState(emptyListInfoMessage = emptyResultMessage)

        whenever(recipesListUseCase.loadRecipes()).thenReturn(Observable.just(emptyList()))
        whenever(staticResourcesProvider.getStaticStringResource(R.string.recipes_empty_list)).thenReturn(emptyResultMessage)

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(emptyResult)
        }
    }

    @Test
    fun verifyLoadErrorState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val errorResultMessage = "There was an error fetching data. Please try again later."
        val errorResult = RecipesViewState(errorInfoMessage = errorResultMessage)

        whenever(recipesListUseCase.loadRecipes()).thenReturn(Observable.error(RuntimeException()))
        whenever(staticResourcesProvider.getStaticStringResource(R.string.generic_network_error)).thenReturn(errorResultMessage)

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(errorResult)
        }
    }

    @Test
    fun verifyShowFilterInfo() {

        val position = 0

        recipesViewModel.dispatch(RecipesAction.ShowSnackBarFilterInfo(position))

        whenever(recipesListUseCase.getRecipeFilter(position)).thenReturn(Observable.just("filter0"))
    }

}