package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bilal.masterly.R


@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    // optional callbacks for actions
    onAnalyticsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProClick: () -> Unit = {},
) {
    // Use a Surface so the bar uses the theme's surface color and provides proper content color
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Masterly",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Track your journey to mastery",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }

            // action icons + labels
            DifferentScreenOption(
                text = "Analytics",
                id = R.drawable.ic_analytics,
                onClick = onAnalyticsClick
            )

            DifferentScreenOption(
                text = "Settings",
                id = R.drawable.ic_settings,
                onClick = onSettingsClick
            )

            DifferentScreenOption(
                text = "Pro",
                id = R.drawable.ic_pro,
                highlight = true, // shows primary color for pro
                onClick = onProClick
            )
        }
    }
}