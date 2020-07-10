package com.mohsen.apk.wetterkleidung.network.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.network.intercepter.AddHeaderParametersInterceptor
import com.mohsen.apk.wetterkleidung.network.intercepter.NoInternetInterceptor
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteServiceImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val APP_API = "appid"

@Module
class NetworkModule(private val context: Context) {

    @Provides
    @Singleton
    @HeaderParametersQualifier
    fun provideHeaderParameters(): Map<String, String> {
        val parameters = mutableMapOf<String, String>()
        parameters[APP_API] = BuildConfig.APP_API
        return parameters
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideNoInternetInterceptor() =
        NoInternetInterceptor(context)

    @Provides
    @Singleton
    fun provideAddHeaderParametersInterceptor(
        @HeaderParametersQualifier parameters: Map<String, String>
    ) = AddHeaderParametersInterceptor(parameters)

    @Provides
    @Singleton
    fun provideJSONConvertorFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CoroutineCallAdapterFactory =
        CoroutineCallAdapterFactory.invoke()

    @Provides
    @Singleton
    fun provideOkHttp(
        noInternetInterceptor: NoInternetInterceptor,
        addHeaderParametersInterceptor: AddHeaderParametersInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(noInternetInterceptor)
            .addInterceptor(addHeaderParametersInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        callAdapterFactory: CoroutineCallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()

    @Provides
    @Singleton
    fun provideWeatherRemoteService(retrofit: Retrofit) =
        WeatherRemoteServiceImpl(retrofit)

}