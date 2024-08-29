package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName

data class HotVideInfo(
    val pic: String,
    val title: String,
    val desc: String,
    val owner: VideoInfoOwner,
    val stat: VideoInfoStat,
    val aid: Long,
    val cid: Long,

)

data class SeasonRankVideoInfo(
    val cover: String,
    val title: String,
    val stat: SeasonVideoInfoStat,
    val rank: Long,
    val rating: String,
    @SerializedName("season_id")
    val seasonID: Long,
    @SerializedName("new_ep")
    val newEp: SeasonVideoEPInfo
)

data class VideoInfo(
    val id: Long,
    val bvid: String,
    val cid: Long,
    val goto: String,
    val uri: String,
    val pic: String,
    val title: String,
    val duration: Long,
    @SerializedName("pubdate")
    val pubDate: Long,
    @SerializedName("is_followed")
    val isFollowed: Long,
    @SerializedName("show_info")
    val showInfo: Long,
    @SerializedName("track_id")
    val trackId: String,
    val stat: VideoInfoStat,
    val owner: VideoInfoOwner
) {
    fun getDurationText(): String {
        if (duration <= 0) {
            return ""
        }
        val second = duration % 60
        val minute = duration / 60 % 60
        return minute.toString() + ':' + String.format("%02d", second)
    }
}

data class VideoInfoStat(
    val view: Long,
    val like: Long,
    @SerializedName("danmaku")
    val danmuku: Long
)

data class SeasonVideoInfoStat(
    val view: Long,
    val follow: Long,
    @SerializedName("series_follow")
    val seriesFollow: Long,
    val danmaku: Long
)

data class SeasonVideoEPInfo(
    val cover: String,
    @SerializedName("index_show")
    val indexShow: String,
)

data class VideoInfoOwner(
    val mid: Long,
    val name: String,
    val face: String
)