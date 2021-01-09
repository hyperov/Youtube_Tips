package com.islam.hesn.youtubetips.category.model.response

data class YoutubeCategoryResponse(
    val etag: String,
    val items: List<VideoItem>,
    val kind: String,
    val pageInfo: PageInfo
)