package com.example.bilitv.http.api

object GlobalState {

    @Volatile
    lateinit var csrfToken: String
}