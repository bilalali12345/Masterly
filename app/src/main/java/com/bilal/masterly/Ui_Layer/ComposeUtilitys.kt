package com.bilal.masterly.Ui_Layer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bilal.masterly.R
import com.bilal.masterly.ui.theme.Purple

@Composable
fun SkillCard() {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "JavaScript Development",
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Purple
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = "1,500 / 10,000 hrs",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp)
            )

            Text(
                text = "13%",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 2.dp)
            )


        }

        LinearProgressIndicator(
            progress = 0.13f,
            color = Purple,
            trackColor = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .height(6.dp)
        )
    }
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 15.dp, horizontal = 10.dp)
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text("Masterly", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold , softWrap = false)
            Text(
                "Track your journey to mastery",
                color = Color.Gray,
                fontSize = 12.sp,
                softWrap = true,
                maxLines = 2
            )
        }
        DifferentScreenOption(text = "Analytics", id = R.drawable.ic_analytics)
        DifferentScreenOption(text = "Settings", id = R.drawable.ic_settings)
        DifferentScreenOption(text = "Pro", id = R.drawable.ic_pro)


    }

}

@Composable
fun DifferentScreenOption(text: String, @DrawableRes id: Int) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            tint = if (id == R.drawable.ic_pro) {
                Purple
            } else {
                Color.White
            },
            modifier = Modifier.size(28.dp).padding(horizontal = 2.dp)
        )
        Text(text = text, color = Color.White, fontSize = 12.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Previewer(modifier: Modifier = Modifier) {
    TopBar()
}