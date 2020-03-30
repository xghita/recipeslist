package com.weightwatchers

import android.os.Bundle
import com.weightwatchers.base.BaseActivity
import com.weightwatchers.ww_exercise_01.R


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}