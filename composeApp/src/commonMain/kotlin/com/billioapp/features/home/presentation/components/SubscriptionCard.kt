package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.BillItemModel
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import org.jetbrains.compose.resources.painterResource

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    width: Dp,
    height: Dp,
    corner: Dp,
    modifier: Modifier = Modifier,
    onDeleteClicked: (String) -> Unit = {}
) {
    // Color priority logic with safe fallback: predefined_bills.primary_color ?: subscription.color
    // Wrap hex parsing with try-catch; keep MaterialTheme read outside to avoid Compose restrictions
    val displayColorHex = subscription.predefinedBills?.primaryColor ?: subscription.color
    val parsedColor = try {
        displayColorHex?.let { hexToColor(it) }
    } catch (e: Exception) {
        null
    }
    val displayColor = parsedColor ?: MaterialTheme.colorScheme.primary
    val onColor = if (displayColor.luminance() > 0.5f) HomeColors.TextPrimary else Color.White
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
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(displayColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol ikon: Material ikon (kategori tabanlı kaynaklar bu varyantta kullanılmıyor)
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                    contentDescription = null,
                    tint = onColor,
                    modifier = Modifier.size(width * 0.12f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Middle text column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    val nameSize = (width.value * (32f / 522f)).sp
                    val amountSize = (width.value * (28f / 522f)).sp

                    Text(
                        text = subscription.name,
                        fontFamily = getBalooFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = nameSize,
                        color = onColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${subscription.amount.toInt()} ${subscription.currency}",
                        fontFamily = getBalooFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = amountSize,
                        color = onColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Right action icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { onDeleteClicked(subscription.id) }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    bill: BillItemModel,
    width: Dp,
    height: Dp,
    corner: Dp,
    modifier: Modifier = Modifier,
    onDeleteClicked: (String) -> Unit = {}
) {
    val displayColor = Color(bill.primaryColorHex)
    val onColor = if (displayColor.luminance() > 0.5f) HomeColors.TextPrimary else Color.White
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
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(displayColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(bill.iconRes),
                    contentDescription = null,
                    tint = onColor,
                    modifier = Modifier.size(width * 0.12f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    val nameSize = (width.value * (32f / 522f)).sp
                    val amountSize = (width.value * (28f / 522f)).sp

                    Text(
                        text = bill.name,
                        fontFamily = getBalooFontFamily(),
                        fontWeight = FontWeight.Medium,
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
                        fontSize = amountSize,
                        color = onColor,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = amountShadow
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { /* TODO: edit from bill */ }) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { onDeleteClicked(bill.id) }) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
            }
        }
    }
}

private val amountShadow = Shadow(
    color = Color(0x33000000),
    offset = Offset(1.5f, 1.5f),
    blurRadius = 2f
)

// Kategori ikon eşleme, BillItemModel varyantında HomeViewModel üzerinden sağlanır.

private fun generateSubscriptionColor(input: String): Color {
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

private fun blendColorForText(bg: Color): Color {
    return if (bg.luminance() > 0.5f) HomeColors.Primary else Color.White
}

private fun hexToColor(hex: String?): Color? {
    if (hex.isNullOrBlank() || !hex.startsWith("#")) return null
    val cleanHex = hex.substring(1)
    return try {
        when (cleanHex.length) {
            6 -> {
                val rgb = cleanHex.toInt(16)
                Color(0xFF000000.toInt() or rgb)
            }
            8 -> {
                val argb = cleanHex.toLong(16).toInt()
                Color(argb)
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}