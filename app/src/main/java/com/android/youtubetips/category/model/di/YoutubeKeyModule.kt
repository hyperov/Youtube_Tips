package com.android.youtubetips.category.model.di

import com.android.youtubetips.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object YoutubeKeyModule {

    @Provides
    fun getYoutubeApiKey(): String {
        return BuildConfig.YOUTUBE_API_KEY
    }
}