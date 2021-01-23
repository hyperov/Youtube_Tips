package com.nabil.youtubetips.category.model.response

data class VideoItem(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)