package com.weightwatchers.presentation.recipes.adapter

import androidx.recyclerview.widget.DiffUtil
import com.weightwatchers.data.network.model.Recipe

class RecipeDiffCallback(
        private val old: List<Recipe>,
        private val new: List<Recipe>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
        return old[oldIndex].title == new[newIndex].title
    }

    override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean {
        return old[oldIndex] == new[newIndex]
    }
}