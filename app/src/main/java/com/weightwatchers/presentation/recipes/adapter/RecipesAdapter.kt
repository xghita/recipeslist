package com.weightwatchers.presentation.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.weightwatchers.data.network.model.Recipe
import com.weightwatchers.data.network.model.RecipeDto
import com.weightwatchers.ww_exercise_01.BuildConfig
import com.weightwatchers.ww_exercise_01.R
import kotlinx.android.synthetic.main.item_recipe.view.*

typealias ClickListener = (Int) -> Unit

class RecipesAdapter(val clickListener: ClickListener) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    private var recipes = emptyList<RecipeDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipe, parent, false) as ViewGroup
        val viewHolder = ViewHolder(itemContainer)
        itemContainer.setOnClickListener { clickListener(viewHolder.adapterPosition) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.itemView.recipeName.text = recipe.title
        Picasso.get().load(recipe.photoUrl).error(R.drawable.ic_recipe_placeholder).into(holder.itemView.recipeImage)
    }

    override fun getItemCount() = recipes.size

    fun updateRecipes(recipes: List<RecipeDto>) {
        val diffResult = DiffUtil.calculateDiff(RecipeDiffCallback(this.recipes, recipes))
        this.recipes = recipes
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemViewGroup: ViewGroup) : RecyclerView.ViewHolder(itemViewGroup)
}