package com.example.bilitv.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.api.PassportApi
import com.jing.bilibilitv.http.data.ApplyQrCodeResponse
import com.jing.bilibilitv.http.data.QrCodeStatus
import com.jing.bilibilitv.http.data.UserInfo
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
    private val loginAPI: PassportApi
) : ViewModel() {
    private val _userInfo = MutableStateFlow<UserInfo?>(value = null)
    private val _qrCodeData = MutableStateFlow<ApplyQrCodeResponse?>(value = null)

    val userInfo = _userInfo.asStateFlow()
    val qrCodeData = _qrCodeData.asStateFlow()
    private var qrCodeKey: String? = null

    fun fetchUser() {
        viewModelScope.launch {
            val userInfo = bilibiliApi.getLoginUserInfo().data
            _userInfo.value = userInfo
        }
    }

    fun requestQRCodeData() {
        viewModelScope.launch {
            val qrcode = loginAPI.applyQrCode().data
            _qrCodeData.value = qrcode
            qrCodeKey = qrcode?.qrCodeKey
            qrCodeKey?.let {
                pullQrCode()
            }
        }
    }

    fun pullQrCode() {
        viewModelScope.launch {
            val data = loginAPI.pollQrCodeStatus(qrCodeKey!!).data
            if (data?.refreshToken?.isNotEmpty() == true) {
                fetchUser()
            } else {
                delayTask(1) {
                    pullQrCode()
                }
            }
        }
    }

    suspend fun delayTask(seconds: Long, task: () -> Unit) {
        delay(seconds * 1000)
        task()
    }
}