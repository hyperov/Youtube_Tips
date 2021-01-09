package com.islam.hesn.youtubetips.category.model.repo

import com.islam.hesn.youtubetips.category.model.response.YoutubeCategoryResponse
import javax.inject.Inject

class YoutubeRepoImpl @Inject constructor(
    private val apis: YoutubeApis,
    private val apiKey: String
) : YoutubeRepo {

    override suspend fun getYoutubeCategoryVideosById(
        idList: String
    ): YoutubeCategoryResponse {
        return apis.getYoutubeCategoryVideosById(idList, apiKey)
    }


}