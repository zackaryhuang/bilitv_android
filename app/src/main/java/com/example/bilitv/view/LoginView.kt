package com.example.bilitv.view

import android.graphics.Bitmap
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.example.bilitv.ui.theme.BiliPink
import com.example.bilitv.view.model.LoginUserViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun LoginView(viewModel: LoginUserViewModel) {

    val qrCodeData = viewModel.qrCodeData.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.requestQRCodeData()
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 500.dp)
        ) {
            qrCodeData.value?.let {
                val bitmap = generateQRCodeBitmap(it.url, 500, 500)
                Image(
                    bitmap = bitmap,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .padding(vertical = 100.dp)
                .background(Color.Gray)
        )

        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 500.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Row {
                    Text(
                        text = "使用手机登录",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "哔哩哔哩",
                        color = BiliPink,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "扫描二维码登录",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier
                    .height(10.dp)
                )

                Text(
                    text = "你不会还没有安装哔哩哔哩吧？快去 App Store 下载吧!",
                    fontSize = 12.sp
                )
            }
        }
    }
}

fun generateQRCodeBitmap(text: String, width: Int, height: Int): ImageBitmap {
    val qrCodeWriter = QRCodeWriter()
    val hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
    val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints)

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }

    return bitmap.asImageBitmap()
}