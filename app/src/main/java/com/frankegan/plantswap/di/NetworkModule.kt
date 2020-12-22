package com.frankegan.plantswap.di

import com.frankegan.plantswap.data.remote.AuthenticationInterceptor
import com.frankegan.plantswap.data.remote.PlantSwapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    fun provideOkHttpClient(
        authenticationInterceptor: AuthenticationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providePlantSwapService(okHttpClient: OkHttpClient): PlantSwapService {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5001/plant-swap-dc8be/us-central1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}