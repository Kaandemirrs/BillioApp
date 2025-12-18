package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.BillItemModel
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.domain.model.subscriptions.Subscription

@Composable
fun BillListSection(
    bills: List<BillItemModel>,
    modifier: Modifier = Modifier,
    onDeleteClicked: (String) -> Unit = {},
    onCheckClicked: (BillItemModel) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(42.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
    ) {
        BoxWithConstraints(modifier = Modifier.padding(HomeSpacing.SectionSpacing)) {
            val contentWidth = maxWidth
            val peek = contentWidth * 0.09f
            val cardWidth = contentWidth - (peek * 2)
            val computedHeight = cardWidth * (300f / 522f)
            val minHeight = 200.dp
            val cardHeight = if (computedHeight < minHeight) minHeight else computedHeight
            val cardCorner = cardWidth * (60f / 522f)

            val titleSize = (contentWidth.value * (56f / 1024f)).sp

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "BİLL LİST",
                    fontFamily = getBalooFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = titleSize,
                    color = HomeColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(HomeSpacing.SectionSpacing))

                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = peek),
                    horizontalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing)
                ) {
                    items(bills) { bill ->
                        SubscriptionCard(
                            bill = bill,
                            width = cardWidth,
                            height = cardHeight,
                            corner = cardCorner,
                            onDeleteClicked = { id -> onDeleteClicked(bill.id) },
                            onCheckClick = { onCheckClicked(bill) }
                        )
                    }
                }
            }
        }
    }
}

// LargeBillCard removed: obsolete design; Bill list now uses SubscriptionCard exclusively.
