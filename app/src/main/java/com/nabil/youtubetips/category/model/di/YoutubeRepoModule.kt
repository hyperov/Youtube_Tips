package com.nabil.youtubetips.category.model.di

import com.nabil.youtubetips.category.model.repo.YoutubeRepo
import com.nabil.youtubetips.category.model.repo.YoutubeRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class YoutubeRepoModule {

    @Binds
    abstract fun provideYoutubeRepoModule(repo: YoutubeRepoImpl): YoutubeRepo

}