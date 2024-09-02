package com.example.bilitv.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
    modifier: Modifier = Modifier,
    item: DisplayableData,
    onClick: ((DisplayableData) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box{
            RemoteImage(
                url = item.cover,
                modifier = Modifier
                    .aspectRatio(16.0F / 9.0F, matchHeightConstraintsFirst = true)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                if (item.danmaku.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_danmaku_count),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = item.danmaku)
                }
                if (item.view.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.icon_play_count),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
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
            minLines = 2,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
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