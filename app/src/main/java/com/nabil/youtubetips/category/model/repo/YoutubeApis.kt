package com.nabil.youtubetips.category.model.repo

import com.nabil.youtubetips.category.model.response.YoutubeCategoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApis {

    @GET("videos")
    suspend fun getYoutubeCategoryVideosById(
        @Query("id") idList: String,
        @Query("key") key: String,
        @Query("part") part: String = "snippet",
    ): YoutubeCategoryResponse


    companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    }

}