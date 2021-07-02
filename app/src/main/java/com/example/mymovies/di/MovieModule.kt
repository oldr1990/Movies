package com.example.mymovies.di

import com.example.mymovies.data.Constants.BASE_URL
import com.example.mymovies.interfaces.OmdbAPI
import com.example.mymovies.data.repository.DefaultRepository
import com.example.mymovies.data.repository.Repository
import com.example.mymovies.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MovieModule {

    @Singleton
    @Provides
    fun provideOkHTTP(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .callTimeout(20, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideOmdbAPI(okClient: OkHttpClient): OmdbAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okClient)
        .build()
        .create(OmdbAPI::class.java)

    @Singleton
    @Provides
    fun provideRepository(api: OmdbAPI): Repository =
        DefaultRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}