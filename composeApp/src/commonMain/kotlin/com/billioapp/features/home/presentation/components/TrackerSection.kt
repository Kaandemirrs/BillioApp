package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_money
import billioapp.composeapp.generated.resources.ic_add

import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.features.home.presentation.TrackerCategory
import com.billioapp.features.home.presentation.TrackerModel
import com.billioapp.domain.model.subscriptions.Subscription
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrackerSection(
    model: TrackerModel,
    subscriptions: List<Subscription> = emptyList(),
    modifier: Modifier = Modifier
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(1.45f)) {
                    TrackerDonut(model = model)
                }
                Column(modifier = Modifier.weight(0.55f)) {
                    RegularBillsList(categories = model.categories, subscriptions = subscriptions)
                }
            }
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

                if (total <= 0.0 || model.categories.isEmpty()) {
                    drawArc(
                        color = HomeColors.Primary.copy(alpha = 0.15f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke)
                    )
                } else {
                    for (c in model.categories) {
                        val sweep = (c.amount / total * 360f).toFloat()
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
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Toplam",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = getBalooFontFamily()),
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
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = getBalooFontFamily()),
                    fontWeight = FontWeight.Bold,
                    color = HomeColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RegularBillsList(
    categories: List<TrackerCategory>,
    subscriptions: List<Subscription> = emptyList()
) {
    val legendItems = buildList {
        for (cat in categories) add(cat.name to Color(cat.colorHex))
        for (subscription in subscriptions) add(subscription.name to generateSubscriptionColorForTracker(subscription.category ?: subscription.name))
    }
    val pages = if (legendItems.isEmpty()) emptyList() else legendItems.chunked(5)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Düzenli faturalar",
            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = getBalooFontFamily()),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = HomeColors.TextPrimary
        )

        if (pages.isEmpty()) {
            // No items; show nothing
        } else {
            RegularBillsPager(pages = pages)
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
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = getBalooFontFamily()),
            fontSize = 15.sp,
            color = HomeColors.TextPrimary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RegularBillsPager(pages: List<List<Pair<String, Color>>>) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                for ((name, color) in pages[page]) {
                    LegendItem(name = name, color = color)
                }
            }
        }

        if (pages.size > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in pages.indices) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (i == pagerState.currentPage) HomeColors.Primary else HomeColors.TextSecondary.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }
        }
    }
}

private fun generateSubscriptionColorForTracker(input: String): Color {
    val colors = listOf(
        Color(0xFF6366F1),
        Color(0xFF8B5CF6),
        Color(0xFFEC4899),
        Color(0xFFEF4444),
        Color(0xFFF97316),
        Color(0xFFF59E0B),
        Color(0xFF10B981),
        Color(0xFF06B6D4),
        Color(0xFF3B82F6),
        Color(0xFF8B5A2B),
    )
    val hash = input.hashCode()
    val index = kotlin.math.abs(hash) % colors.size
    return colors[index]
}