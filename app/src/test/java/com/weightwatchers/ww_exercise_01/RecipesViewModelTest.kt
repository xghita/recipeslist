package com.weightwatchers.ww_exercise_01

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
import java.lang.RuntimeException

class RecipesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private val recipesList = listOf(RecipeDto("filter", "image_url", "title"),
            RecipeDto("filter", "image_url", "title"))

    private lateinit var recipesViewModel: RecipesViewModel
    private val testObserver = mock<Observer<RecipesViewState>>()
    private val recipesListUseCase = mock<RecipesListUseCase>()
    private val resourcesProvider = mock<StaticResourcesProvider>()

    @Before
    fun setUp() {
        recipesViewModel = RecipesViewModel(RecipesViewState(), recipesListUseCase)
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

//        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyLoadEmptyState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val emptyResult = RecipesViewState(emptyListInfoMessage = resourcesProvider.getStaticStringResource(R.string.recipes_empty_list))

        whenever(recipesListUseCase.loadRecipes()).thenReturn(Observable.just(emptyList()))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
//            verify(testObserver).onChanged(RecipesViewState())
            verify(testObserver).onChanged(emptyResult)
        }

//        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyLoadErrorState() {
        val recipesLoading = RecipesViewState(isLoading = true)
        val errorResult = RecipesViewState(errorInfoMessage = "There was an error fetching data. Please try again later.")

        whenever(recipesListUseCase.loadRecipes()).thenReturn(Observable.error(RuntimeException()))

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
        testSchedulerRule.triggerActions()

        inOrder(testObserver) {
            verify(testObserver).onChanged(recipesLoading)
            verify(testObserver).onChanged(errorResult)
        }

        //        verifyNoMoreInteractions(testObserver)
    }

    @Test
    fun verifyShowFilterInfo() {

        val filter = "[\\\"contentTags.food_cuisines.tags:Italian\\\"]"
        val showSnackBarState = RecipesViewState()
        val snackBarDismissed = RecipesViewState()

        recipesViewModel.dispatch(RecipesAction.ShowSnackBarFilterInfo(0))


        whenever(recipesListUseCase.getRecipeFilter(0)).thenReturn(Observable.just("filter"))

    }

}