package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    val mid: Long,
    @SerializedName("item")
    val items: List<VideoInfo>
)

data class PopularResponse(
    @SerializedName("list")
    val items: List<HotVideInfo>,
    @SerializedName("no_more")
    val hasMore: Boolean
)

data class SeasonRankResponse(
    @SerializedName("list")
    val items: List<SeasonRankVideoInfo>,
    val note: String
)