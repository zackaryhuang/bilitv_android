package com.jing.bilibilitv.http.data

import com.google.gson.annotations.SerializedName


data class HistoryResponse(
    @SerializedName("cursor")
    val cursor: HistoryCursor,
    @SerializedName("list")
    val list: List<HistoryItem>,
    @SerializedName("tab")
    val tab: List<HistoryTab>
)

data class WatchLaterResponse(
    @SerializedName("list")
    val list: List<WatchLaterItem>
)

data class WatchLaterItem(
    @SerializedName("title")
    override val title: String,
    @SerializedName("cid")
    val cidLong: Long,
    @SerializedName("aid")
    val aidLong: Long,
    @SerializedName("stat")
    val stat: VideoStatInfo,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("owner")
    val owner: VideoOwnerInfo,
): DisplayableAndPlayableData {
    override val cover: String
        get() = this.pic
    override val ownerFace: String
        get() = this.owner.face
    override val ownerName: String
        get() = this.owner.name
    override val danmaku: String
        get() = this.stat.danmaku.toCountString()
    override val view: String
        get() = this.stat.view.toCountString()
    override val aid: String
        get() = this.aidLong.toString()
    override val cid: String
        get() = this.cidLong.toString()
    override val bvid: String?
        get() = null
    override val seasonID: String?
        get() = null
    override val episodeID: String?
        get() = null
}

data class HistoryCursor(
    @SerializedName("business")
    val business: String,
    @SerializedName("max")
    val max: Long,
    @SerializedName("ps")
    val ps: Long,
    @SerializedName("view_at")
    val viewAt: Long
)

data class HistoryItem(
    @SerializedName("author_face")
    val authorFace: String,
    @SerializedName("author_mid")
    val authorMid: Long,
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("badge")
    val badge: String,
    @SerializedName("cover")
    override val cover: String,
    @SerializedName("current")
    val current: String,
    @SerializedName("duration")
    val duration: Long,
    @SerializedName("history")
    val history: HistoryHistory,
    @SerializedName("is_fav")
    val isFav: Long,
    @SerializedName("is_finish")
    val isFinish: Long,
    @SerializedName("kid")
    val kid: Long,
    @SerializedName("live_status")
    val liveStatus: Long,
    @SerializedName("long_title")
    val longTitle: String,
    @SerializedName("new_desc")
    val newDesc: String,
    @SerializedName("progress")
    val progress: Long,
    @SerializedName("show_title")
    val showTitle: String,
    @SerializedName("tag_name")
    val tagName: String,
    @SerializedName("title")
    override val title: String,
    @SerializedName("total")
    val total: Long,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("videos")
    val videos: Long,
    @SerializedName("view_at")
    val viewAt: Long
): DisplayableAndPlayableData {
    override val ownerName: String
        get() = this.authorName
    override val ownerFace: String
        get() = this.authorFace
    override val danmaku: String
        get() = ""
    override val view: String
        get() = ""
    override val aid: String?
        get() = null
    override val bvid: String
        get() = this.history.bvid
    override val cid: String
        get() = this.history.cid.toString()
    override val episodeID: String
        get() = this.history.epid.toString()
    override val seasonID: String?
        get() = null
}

data class HistoryTab(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)

data class HistoryHistory(
    @SerializedName("business")
    val business: String,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("cid")
    val cid: Long,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("epid")
    val epid: Long,
    @SerializedName("oid")
    val oid: Long,
    @SerializedName("page")
    val page: Long,
    @SerializedName("part")
    val part: String
)