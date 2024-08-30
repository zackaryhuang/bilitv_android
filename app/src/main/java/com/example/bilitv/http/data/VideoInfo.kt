package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import kotlin.reflect.KProperty

data class HotVideInfo(
    val pic: String,
    override val title: String,
    val desc: String,
    val owner: VideoInfoOwner,
    val stat: VideoInfoStat,
    val aid: Long,
    val cid: Long,
): DisplayableData {
    override val cover: String
        get() = this.pic
    override val view: String
        get() = this.stat.view.toCountString()
    override val danmaku: String
        get() = this.stat.danmuku.toCountString()
    override val ownerFace: String
        get() = this.owner.face
    override val ownerName: String
        get() = this.owner.name
}

fun Long.toCountString(): String {
    return if (this >= 10_000) {
        val roundedValue = (this / 10_000.0).let {
            kotlin.math.round(it * 10) / 10  // 保留一位小数并四舍五入
        }
        "$roundedValue 万"
    } else {
        this.toString() // 小于 10,000，直接返回原值
    }
}

interface DisplayableData {
    val title: String
    val cover: String
    val danmaku: String
    val view: String
    val ownerFace: String
    val ownerName: String
}

data class SeasonRankVideoInfo(
    override val cover: String,
    override val title: String,
    val stat: SeasonVideoInfoStat,
    val rank: Long,
    val rating: String,
    @SerializedName("season_id")
    val seasonID: Long,
    @SerializedName("new_ep")
    val newEp: SeasonVideoEPInfo
): DisplayableData {
    override val danmaku: String
        get() = this.stat.danmaku.toString()
    override val view: String
        get() = this.stat.view.toString()
    override val ownerFace: String
        get() = this.newEp.cover
    override val ownerName: String
        get() = this.newEp.indexShow
}

data class VideoInfo(
    val id: Long,
    val bvid: String,
    val cid: Long,
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
    val stat: VideoInfoStat,
    val owner: VideoInfoOwner
): DisplayableData {
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
        get() = this.stat.danmuku.toCountString()
    override val view: String
        get() = this.stat.view.toCountString()
    override val ownerFace: String
        get() = this.owner.face
    override val ownerName: String
        get() = this.owner.name
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