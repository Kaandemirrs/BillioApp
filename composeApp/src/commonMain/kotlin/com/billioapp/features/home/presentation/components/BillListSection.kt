package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import org.jetbrains.compose.resources.painterResource

@Composable
fun BillListSection(
    bills: List<BillItemModel>,
    subscriptions: List<Subscription> = emptyList(),
    modifier: Modifier = Modifier,
    onDeleteClicked: (String) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(42.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.BillListGreen)
    ) {
        BoxWithConstraints(modifier = Modifier.padding(HomeSpacing.SectionSpacing)) {
            val contentWidth = maxWidth

            val peek = contentWidth * 0.09f

            val cardWidth = contentWidth - (peek * 2)
            val cardHeight = cardWidth * (300f / 522f)
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
                        LargeBillCard(
                            bill = bill,
                            width = cardWidth,
                            height = cardHeight,
                            corner = cardCorner
                        )
                    }
                    items(subscriptions) { subscription ->
                        SubscriptionCard(
                            subscription = subscription,
                            width = cardWidth,
                            height = cardHeight,
                            corner = cardCorner,
                            onDeleteClicked = onDeleteClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LargeBillCard(
    bill: BillItemModel,
    width: Dp,
    height: Dp,
    corner: Dp,
    modifier: Modifier = Modifier
) {
    val primaryColor = Color(bill.primaryColorHex)
    val onColor = if (primaryColor.luminance() > 0.5f) HomeColors.TextPrimary else Color.White
    val nameSize = (width.value * (34f / 522f)).sp
    val amountSize = (width.value * (42f / 522f)).sp

    Card(
        modifier = modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(corner),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(HomeSpacing.SectionSpacing)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(corner))
                    .background(primaryColor)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Icon (fallback to Receipt if painter fails)
                androidx.compose.material3.Icon(
                    painter = painterResource(bill.iconRes),
                    contentDescription = null,
                    tint = onColor,
                    modifier = Modifier.size(height * 0.30f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Middle text column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = bill.name,
                        fontFamily = getBalooFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = nameSize,
                        color = onColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = bill.amountText,
                        fontFamily = getBalooFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = (width.value * (30f / 522f)).sp,
                        color = onColor
                    )
                }

                // Action icons at the far right
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.IconButton(onClick = {}) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )
                    }
                    androidx.compose.material3.IconButton(onClick = {}) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}