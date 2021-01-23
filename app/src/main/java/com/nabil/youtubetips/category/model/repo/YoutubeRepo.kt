package com.nabil.youtubetips.category.model.repo

import com.nabil.youtubetips.category.model.response.YoutubeCategoryResponse
import retrofit2.http.Query

interface YoutubeRepo {

    suspend fun getYoutubeCategoryVideosById(
        @Query("id") idList: String
    ): YoutubeCategoryResponse
}