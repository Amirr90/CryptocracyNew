package com.e.cryptocracy.di

import com.e.cryptocracy.api.CoinApi
import com.e.cryptocracy.api.CoinApi.Companion.BASE_URL
import com.e.cryptocracy.redux.Store
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.utils.AppPrefs
import com.e.cryptocracy.utils.CustomInterceptor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BaseAppModule {
    @Provides
    @Singleton
    fun provideHttpLogger(appPrefs: AppPrefs): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(CustomInterceptor(appPrefs))
            .build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CoinApi {
        return retrofit.create(CoinApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStore(): Store<ApplicationState> = Store(ApplicationState())

    @Provides
    @Singleton
    fun provideFirebaseDbInstance() = Firebase.firestore
}
