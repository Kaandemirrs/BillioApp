package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_money
import billioapp.composeapp.generated.resources.ic_add
import billioapp.composeapp.generated.resources.tre
// (removed) import billioapp.composeapp.generated.resources.tre
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.features.home.presentation.TrackerCategory
import com.billioapp.features.home.presentation.TrackerModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrackerSection(
    model: TrackerModel,
    modifier: Modifier = Modifier,
    onLimitIconClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(HomeSpacing.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
    ) {
        Column(
            modifier = Modifier.padding(HomeSpacing.SectionSpacing),
            verticalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing)
        ) {
            // Üst bölüm: Donut solda geniş, düzenli faturalar sağda dar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(1.45f)) {
                    TrackerDonut(model = model)
                }
                Column(modifier = Modifier.weight(0.55f)) {
                    RegularBillsList(categories = model.categories)
                }
            }

            // Alt: Limit ve Kalan metinleri
            LimitRemainingFooter(limitText = "3000 TL", remainingText = "1200 TL", onLimitIconClick = onLimitIconClick)
        }
    }
}

@Composable
private fun TrackerDonut(model: TrackerModel) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val donutSize = maxWidth * 0.70f
        val strokeWidth = (donutSize * 0.08f)

        Box(
            modifier = Modifier.size(donutSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val total = model.categories.sumOf { it.amount }
                val stroke = strokeWidth.toPx()
                var startAngle = -90f
                model.categories.forEach { c ->
                    val sweep = if (total > 0) (c.amount / total * 360f).toFloat() else 0f
                    drawArc(
                        color = Color(c.colorHex),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = stroke)
                    )
                    startAngle += sweep
                }
            }

            // Merkez içerik: Toplam, ikon ve miktar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Toplam",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HomeColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(Res.drawable.ic_money),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "${model.totalAmount.toInt()}.00${model.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HomeColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RegularBillsList(categories: List<TrackerCategory>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Düzenli faturalar",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = HomeColors.TextPrimary
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            categories.forEach { cat ->
                LegendItem(name = cat.name, color = Color(cat.colorHex))
            }
        }
    }
}

@Composable
private fun LimitRemainingFooter(limitText: String, remainingText: String, onLimitIconClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Limit",
                style = MaterialTheme.typography.bodyMedium,
                color = HomeColors.TextPrimary
            )
            Text(
                text = limitText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFC54646)
            )
            // Küçük artı ikonu (metin boyutuna yakın)
            Image(
                painter = painterResource(Res.drawable.tre),
                contentDescription = "Limiti artır",
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onLimitIconClick() }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Kalan",
                style = MaterialTheme.typography.bodyMedium,
                color = HomeColors.TextPrimary
            )
            Text(
                text = remainingText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF62C546)
            )
        }
    }
}

@Composable
private fun LegendItem(name: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .height(12.dp)
                .aspectRatio(1f)
                .background(color, shape = RoundedCornerShape(2.dp))
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            color = HomeColors.TextPrimary
        )
    }
}