package com.weightwatchers.di

import com.weightwatchers.data.network.api.ApiDecorator
import com.weightwatchers.data.repository.RecipesRepository
import com.weightwatchers.domain.recipes.RecipesListUseCase
import com.weightwatchers.domain.recipes.RecipesViewModelFactory
import com.weightwatchers.presentation.recipes.RecipesViewModel
import com.weightwatchers.presentation.recipes.state.RecipesViewState
import com.weightwatchers.ww_exercise_01.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
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

                single { ApiDecorator(get()) }

                single { RecipesRepository(get()) }

                single { RecipesListUseCase(get()) }

                single { RecipesViewState() }

                factory { RecipesViewModelFactory(get(), get()) }
            }
}
