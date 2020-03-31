package com.weightwatchers.presentation.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.weightwatchers.base.BaseFragment
import com.weightwatchers.data.model.recipe.RecipeDto
import com.weightwatchers.domain.recipes.RecipesViewModelFactory
import com.weightwatchers.presentation.recipes.adapter.ClickListener
import com.weightwatchers.presentation.recipes.adapter.RecipesAdapter
import com.weightwatchers.presentation.recipes.state.RecipesAction
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.utils.adapter.GridItemSpacingDecoration
import com.weightwatchers.ww_exercise_01.R
import kotlinx.android.synthetic.main.fragment_recipes_list.*
import org.koin.android.ext.android.inject

class RecipesFragment : BaseFragment() {

    private val recipesViewModelFactory: RecipesViewModelFactory by inject()

    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var recipesAdapter: RecipesAdapter

    private val clickListener: ClickListener = this::onRecipeSelected

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesViewModel = ViewModelProvider(viewModelStore, recipesViewModelFactory).get(RecipesViewModel::class.java)

        recipesViewModel.observableState.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                renderState(state)
            }
        })

        recipesAdapter = RecipesAdapter(clickListener)

        setupRecyclerView()

        recipesViewModel.dispatch(RecipesAction.LoadRecipes)
    }

    private fun setupRecyclerView() {

        val spanCount = resources.getInteger(R.integer.grid_span_size)
        val gridSpacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)

        val gridLayoutManager = GridLayoutManager(activity, spanCount)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }

        recipesRv.addItemDecoration(GridItemSpacingDecoration(spanCount, gridSpacing, true))

        recipesRv.adapter = recipesAdapter
        recipesRv.layoutManager = gridLayoutManager
    }

    private fun renderState(state: RecipesViewState) {

        if (state.snackBarFilterInfo != null) {
            showSnackBarFilterInfo(state.snackBarFilterInfo)
        }

        with(state) {
            when {
                isLoading -> {
                    renderLoadingState()
                }
                errorInfoMessage != null -> {
                    renderNoDataView(errorInfoMessage)
                }
                emptyListInfoMessage != null -> {
                    renderNoDataView(emptyListInfoMessage)
                }
                else -> renderRecipesList(recipes)
            }
        }
    }

    private fun renderLoadingState() {
        progressLoading.visibility = View.VISIBLE
        infoStateMessageTv.visibility = View.GONE
    }

    private fun renderNoDataView(@StringRes message: Int) {
        progressLoading.visibility = View.GONE
        infoStateMessageTv.text = getString(message)
        infoStateMessageTv.visibility = View.VISIBLE
    }

    private fun renderRecipesList(recipes: List<RecipeDto>) {
        progressLoading.visibility = View.GONE
        infoStateMessageTv.visibility = View.GONE
        recipesAdapter.updateRecipes(recipes)
    }

    private fun showSnackBarFilterInfo(info: String) {

        val snackBar = if (info.isEmpty()) {
            Snackbar.make(recipesFragmentContainer, getString(R.string.recipes_filter_info), Snackbar.LENGTH_SHORT)
        } else {
            Snackbar.make(recipesFragmentContainer, info, Snackbar.LENGTH_SHORT)
        }

        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                recipesViewModel.dispatch(RecipesAction.SnackBarFilterInfoDismissed)
            }
        })

        snackBar.show()
    }

    private fun onRecipeSelected(position: Int) {
        recipesViewModel.dispatch(RecipesAction.ShowSnackBarFilterInfo(position))
    }
}
