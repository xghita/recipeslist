package com.weightwatchers.data.network.model.recipe

import com.github.pozo.KotlinBuilder


@KotlinBuilder
data class Recipe(val title: String, val image: String, val filter: String)
