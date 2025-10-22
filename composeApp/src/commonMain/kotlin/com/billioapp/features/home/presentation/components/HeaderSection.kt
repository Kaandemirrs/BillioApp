package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_logo
import billioapp.composeapp.generated.resources.ic_notification
import billioapp.composeapp.generated.resources.onboarding_illustration
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import org.jetbrains.compose.resources.painterResource
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    userName: String = "Kaan"
) {
    val dateText = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date.let { d -> "${d.dayOfMonth.toString().padStart(2, '0')}/${d.monthNumber.toString().padStart(2, '0')}/${d.year}" }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.onboarding_illustration),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(HomeSpacing.ItemSpacing))
            ColumnText(greeting = "Merhaba $userName", dateText = dateText)
        }

        Image(
            painter = painterResource(Res.drawable.ic_notification),
            contentDescription = "Bildirim",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(HomeColors.Card)
        )
    }
}

@Composable
private fun ColumnText(greeting: String, dateText: String) {
    androidx.compose.foundation.layout.Column {
        Text(
            text = greeting,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = HomeColors.TextPrimary
        )
        Text(
            text = dateText,
            style = MaterialTheme.typography.bodySmall,
            color = HomeColors.TextSecondary
        )
    }
}