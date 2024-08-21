package com.example.bilitv.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
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
                .background(Color.Yellow)
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

        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 500.dp)
                .background(Color.Green)
        ) {

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