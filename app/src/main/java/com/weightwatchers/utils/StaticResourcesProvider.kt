package com.weightwatchers.utils

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes

object StaticResourcesProvider {

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun getStaticStringResource(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }

    fun getStaticIntResource(@IntegerRes intId: Int): Int {
        return context.resources.getInteger(intId)
    }

    fun getStaticDimenResource(@DimenRes dimenId: Int): Int {
        return context.resources.getDimensionPixelSize(dimenId)
    }

}