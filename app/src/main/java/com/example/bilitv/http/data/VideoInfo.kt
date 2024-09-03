package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName

data class HotVideInfo(
    val pic: String,
    override val title: String,
    val desc: String,
    val owner: VideoOwnerInfo,
    val stat: VideoStatInfo,
    override val aid: String,
    override val cid: String,
): DisplayableAndPlayableData {
    override val cover: String
        get() = this.pic
    override val view: String
        get() = this.stat.view.toCountString()
    override val danmaku: String
        get() = this.stat.danmaku.toCountString()
    override val ownerFace: String
        get() = this.owner.face
    override val ownerName: String
        get() = this.owner.name
    override val bvid: String?
        get() = null
    override val seasonID: String?
        get() = null
    override val episodeID: String?
        get() = null
    override val roomID: String?
        get() = null
}

fun Long.toCountString(): String {
    return if (this >= 100_000_000) {
        val roundedValue = (this / 100_000_000.0).let {
            kotlin.math.round(it * 10) / 10  // 保留一位小数并四舍五入
        }
        "$roundedValue 亿"
    } else if (this >= 10_000_000) {
        val roundedValue = (this / 10_000_000.0).let {
            kotlin.math.round(it * 10) / 10  // 保留一位小数并四舍五入
        }
        "$roundedValue 千万"
    }
    else if (this >= 10_000) {
        val roundedValue = (this / 10_000.0).let {
            kotlin.math.round(it * 10) / 10  // 保留一位小数并四舍五入
        }
        "$roundedValue 万"
    } else {
        this.toString() // 小于 10,000，直接返回原值
    }
}

interface PlayableData {
    val bvid: String?
    val aid: String?
    val cid: String?
    val seasonID: String?
    val episodeID: String?
    val roomID: String?
}

interface DisplayableData {
    val title: String
    val cover: String
    val danmaku: String
    val view: String
    val ownerFace: String
    val ownerName: String
}

interface DisplayableAndPlayableData: DisplayableData, PlayableData {

}

data class SeasonRankVideoInfo(
    override val cover: String,
    override val title: String,
    val stat: SeasonVideoInfoStat,
    val rank: Long,
    val rating: String,
    @SerializedName("season_id")
    val seasonIDLong: Long,
    @SerializedName("new_ep")
    val newEp: SeasonVideoEPInfo
): DisplayableAndPlayableData {
    override val danmaku: String
        get() = this.stat.danmaku.toCountString()
    override val view: String
        get() = this.stat.view.toCountString()
    override val ownerFace: String
        get() = this.newEp.cover
    override val ownerName: String
        get() = this.newEp.indexShow
    override val seasonID: String
        get() = this.seasonIDLong.toString()
    override val episodeID: String?
        get() = null
    override val aid: String?
        get() = null
    override val cid: String?
        get() = null
    override val bvid: String?
        get() = null
    override val roomID: String?
        get() = null
}

data class VideoInfo(
    val id: Long,
    override val bvid: String,
    override val cid: String,
    val goto: String,
    val uri: String,
    val pic: String,
    override val title: String,
    val duration: Long,
    @SerializedName("pubdate")
    val pubDate: Long,
    @SerializedName("is_followed")
    val isFollowed: Long,
    @SerializedName("show_info")
    val showInfo: Long,
    @SerializedName("track_id")
    val trackId: String,
    val stat: VideoStatInfo,
    val owner: VideoOwnerInfo
): DisplayableData, PlayableData {
    fun getDurationText(): String {
        if (duration <= 0) {
            return ""
        }
        val second = duration % 60
        val minute = duration / 60 % 60
        return minute.toString() + ':' + String.format("%02d", second)
    }

    override val cover: String
        get() = this.pic
    override val danmaku: String
        get() = this.stat.danmaku.toCountString()
    override val view: String
        get() = this.stat.view.toCountString()
    override val ownerFace: String
        get() = this.owner.face
    override val ownerName: String
        get() = this.owner.name
    override val aid: String?
        get() = null
    override val episodeID: String?
        get() = null
    override val seasonID: String?
        get() = null
    override val roomID: String?
        get() = null
}

data class VideoStatInfo(
    val view: Long,
    val like: Long,
    @SerializedName("danmaku")
    val danmaku: Long
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

data class VideoOwnerInfo(
    val mid: Long,
    val name: String,
    val face: String
)