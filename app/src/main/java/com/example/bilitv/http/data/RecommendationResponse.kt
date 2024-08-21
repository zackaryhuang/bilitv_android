package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    val mid: Long,
    @SerializedName("item")
    val items: List<VideoInfo>
)