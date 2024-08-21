package com.jing.bilibilitv.http.cookie

import android.util.Log
import com.jing.bilibilitv.http.data.LoginData
import com.tencent.mmkv.MMKV
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import kotlinx.serialization.json.Json

class BilibiliCookieJar: CookieJar {
    private val cookieStore = mutableListOf<Cookie>()

    val jsonFormat = Json {
        var ignoreUnknownKeys = true
        var isLenient = true
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (cookieStore.isNotEmpty()) {
            return cookieStore
        }
        val cookies = mutableListOf<Cookie>()
        val kv = MMKV.defaultMMKV()
        val biliCookieString = kv.decodeString("cookies")
        var biliCookies: List<BiliCookie>? = null
        biliCookieString?.let {
            biliCookies = jsonFormat.decodeFromString(it)
        }

        biliCookies?.forEach { cookie ->
            val cookieBuilder = Cookie.Builder()
                .name(cookie.name)
                .value(cookie.value)
                .domain(cookie.domain)
                .expiresAt(cookie.expiresAt)
                .path(cookie.path)

            if (cookie.hostOnly) {
                cookieBuilder.hostOnlyDomain(cookie.domain)
            }

            if (cookie.httpOnly) {
                cookieBuilder.httpOnly()
            }

            if (cookie.secure) {
                cookieBuilder.secure()
            }

            cookies.add(
                cookieBuilder.build()
            )
        }
        cookieStore.addAll(cookies)
        return  cookieStore
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val biliCookies = mutableListOf<BiliCookie>()
        cookies.forEach {
            Log.d("CookieJar", "name:${it.name} value:${it.value}")
            val biliCookie = BiliCookie(
                domain = it.domain,
                expiresAt = it.expiresAt,
                hostOnly = it.hostOnly,
                httpOnly = it.httpOnly,
                name = it.name,
                path = it.path,
                secure = it.secure,
                value = it.value
            )
            biliCookies += biliCookie
            val kv = MMKV.defaultMMKV()
            kv.encode("cookies", jsonFormat.encodeToString(biliCookies))
        }
    }
}

@Serializable
data class BiliCookie (
    val domain: String,
    val expiresAt: Long,
    val hostOnly: Boolean,
    val httpOnly: Boolean,
    val name: String,
    val path: String,
    val secure: Boolean,
    val value: String
)