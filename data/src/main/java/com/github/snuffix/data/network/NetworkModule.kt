package com.github.snuffix.data.network

import android.content.Context
import com.github.snuffix.data.BuildConfig
import com.github.snuffix.data.network.cache.KeywordsCacheInfo
import com.github.snuffix.data.network.source.KoleoApiService
import com.github.snuffix.data.network.source.RemoteKeywordsSourceImpl
import com.github.snuffix.data.network.source.RemoteStationsSourceImpl
import com.github.snuffix.data.network.cache.StationsCacheInfo
import com.github.snuffix.data.network.source.DistanceMatrixApiService
import com.github.snuffix.data.network.source.DistanceMatrixSourceImpl
import com.github.snuffix.domain.repository.CacheInfo
import com.github.snuffix.domain.repository.Constants
import com.github.snuffix.domain.repository.DistanceMatrixSource
import com.github.snuffix.domain.repository.RemoteKeywordsSource
import com.github.snuffix.domain.repository.RemoteStationsSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named(Constants.KEY_GOOGLE_MAPS_API_KEY)
    fun provideApiKey(): String = BuildConfig.GOOGLE_MAPS_API_KEY

    @Provides
    @Singleton
    @Named("koleo")
    fun provideKoleoRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://koleo.pl/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    @Named("google")
    fun provideGoogleApiRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideApiService(@Named("koleo") retrofit: Retrofit): KoleoApiService =
        retrofit.create(KoleoApiService::class.java)

    @Provides
    @Singleton
    fun provideDistanceApiService(@Named("google") retrofit: Retrofit): DistanceMatrixApiService =
        retrofit.create(DistanceMatrixApiService::class.java)

    @Provides
    @Singleton
    fun provideDistanceMatrixSourceImpl(
        @Named(Constants.KEY_GOOGLE_MAPS_API_KEY) apiKey: String,
        apiService: DistanceMatrixApiService
    ): DistanceMatrixSource =
        DistanceMatrixSourceImpl(apiKey, apiService)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideBaseOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRemoteSource(apiService: KoleoApiService): RemoteStationsSource =
        RemoteStationsSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideRemoteKeywordsSource(apiService: KoleoApiService): RemoteKeywordsSource =
        RemoteKeywordsSourceImpl(apiService)

    @Provides
    @Singleton
    @Named(Constants.KEY_KEYWORDS_CACHE)
    fun provideKeywordsCacheInfo(@ApplicationContext context: Context): CacheInfo = KeywordsCacheInfo(context)

    @Provides
    @Singleton
    @Named(Constants.KEY_STATIONS_CACHE)
    fun provideStationsCacheInfo(@ApplicationContext context: Context): CacheInfo = StationsCacheInfo(context)
}