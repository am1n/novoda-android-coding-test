package com.novoda.test.di

import android.content.Context
import com.novoda.test.model.FollowedUsers
import com.novoda.test.model.StackOverflowService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val SERVER_BASE_URL = "https://api.stackexchange.com/2.2/"

    @Singleton
    @Provides
    fun provideStackOverflow(): StackOverflowService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(StackOverflowService::class.java)
    }

    @Singleton
    @Provides
    fun provideFollowedUsers(@ApplicationContext context: Context): FollowedUsers {
        return FollowedUsers(context)
    }

}
