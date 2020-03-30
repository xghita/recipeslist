package com.weightwatchers.data.network.model.recipe

import com.github.pozo.KotlinBuilder

@KotlinBuilder
data class RecipeDto(val title: String, val photoUrl: String, val filter: String)