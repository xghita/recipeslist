package com.weightwatchers.di

import com.weightwatchers.data.api.ApiDecorator
import com.weightwatchers.data.repository.recipes.RecipesMapper
import com.weightwatchers.data.repository.recipes.RecipesRepository
import com.weightwatchers.domain.recipes.RecipesListUseCase
import com.weightwatchers.domain.recipes.RecipesViewModelFactory
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.ww_exercise_01.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApplicationModule {
    val module =
            module {

                single {
                    OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
                }

                single {
                    Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL)
                            .client(get())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }

                single { RecipesViewState() }

                single { RecipesMapper() }

                single { ApiDecorator(get()) }

                single { RecipesRepository(get(), get()) }

                single { RecipesListUseCase(get()) }

                factory { RecipesViewModelFactory(get(), get(), get()) }
            }
}
