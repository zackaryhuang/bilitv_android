package com.example.bilitv.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.example.bilitv.R
import com.jing.bilibilitv.http.data.DisplayableData

@Composable
fun DisplayableVideoItem(
    item: DisplayableData,
    onClick: ((DisplayableData) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .height(240.dp)
            .clickable {
                onClick?.let {
                    it(item)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(width = 200.dp, height = 130.dp)
                    ) {
                        RemoteImage(
                            url = item.cover,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 10.dp, bottom = 5.dp)
                        ) {
                            if (item.danmaku.isNotEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_danmaku_count),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(text = item.danmaku)
                            }
                            if (item.view.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.icon_play_count),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(text = item.view)
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Text(
                        text = item.title,
                        maxLines = 2,
                        color = Color.LightGray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RemoteImage(
                        url = item.ownerFace,
                        modifier = Modifier
                            .size(width = 20.dp, height = 20.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    Text(
                        text = item.ownerName,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}