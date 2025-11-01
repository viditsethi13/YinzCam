package com.example.yinzcamassessment.di

import com.example.yinzcamassessment.common.Constants
import com.example.yinzcamassessment.data.remote.APIService
import com.example.yinzcamassessment.data.repository.APIRepositoryImpl
import com.example.yinzcamassessment.domain.repository.APIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LogoUrlPrefix

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LogoUrlSuffix


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    @LogoUrlPrefix
    fun provideLogoUrl(): String = Constants.LOGO_URL_PREFIX

    @Provides
    @Singleton
    @LogoUrlSuffix
    fun provideLogoUrlSuffix(): String = Constants.LOGO_URL_SUFFIX


    @Provides
    @Singleton
    fun provideAPIService(@BaseUrl baseUrl: String): APIService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAPIRepository(
        apiService: APIService,
        @LogoUrlPrefix logoUrl: String,
        @LogoUrlSuffix logoUrlSuffix: String
    ): APIRepository {
        return APIRepositoryImpl(
            apiService = apiService,
            logoUrl = logoUrl,
            logoUrlSuffix = logoUrlSuffix
        )
    }

}