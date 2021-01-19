package com.android.youtubetips.category.model.di

import com.android.youtubetips.category.model.repo.YoutubeRepo
import com.android.youtubetips.category.model.repo.YoutubeRepoImpl
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