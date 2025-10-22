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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import org.jetbrains.compose.resources.painterResource

@Composable
fun BillListSection(
    bills: List<BillItemModel>,
    modifier: Modifier = Modifier
) {
    // Büyük beyaz kapsayıcı kart (Tracker benzeri)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(42.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
    ) {
        BoxWithConstraints(modifier = Modifier.padding(HomeSpacing.SectionSpacing)) {
            val contentWidth = maxWidth

            // Peek (her iki tarafta kartların bir kısmı gözüksün)
            val peek = contentWidth * 0.09f // %9 kadar kenarlardan kartlar görünsün

            // Büyük fatura kartı ölçüleri (Figma küçük kart oranı referans alınarak büyütüldü)
            val cardWidth = contentWidth - (peek * 2)
            val cardHeight = cardWidth * (250f / 522f)
            val cardCorner = cardWidth * (60f / 522f)

            // Başlık (Figma: Baloo 64, biraz modern ve güçlü görünüm için Bold)
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
    corner: Dp
) {
    Card(
        shape = RoundedCornerShape(corner),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        // Arka plan ve metin kontrastını dinamik belirle
        val bg = Color(bill.primaryColorHex).copy(alpha = 0.90f)
        val prefersDarkText = bg.luminance() > 0.6f
        val nameColor = if (prefersDarkText) Color.Black else Color.White
        val amountColor = nameColor
        val amountShadow = Shadow(
            color = if (prefersDarkText) Color.White.copy(alpha = 0.18f) else Color.Black.copy(alpha = 0.25f),
            offset = Offset(0f, 1.5f),
            blurRadius = 2f
        )

        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(corner))
                .background(bg)
                .padding(HomeSpacing.SectionSpacing)
        ) {
            // Figma 178:2496 — tek parça yuvarlak dikdörtgen zemin, merkezde ikon + isim + tutar
            val iconSize = height * (62f / 250f)
            val nameSize = (width.value * (34f / 522f)).sp
            val amountSize = (width.value * (42f / 522f)).sp

            Column(
                modifier = Modifier.matchParentSize().padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(height * 0.10f))
                Image(
                    painter = painterResource(bill.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.height(height * 0.04f))
                Text(
                    text = bill.name,
                    fontFamily = getBalooFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = nameSize,
                    letterSpacing = 0.01.em,
                    color = nameColor,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(height * 0.045f))
                Text(
                    text = bill.amountText,
                    fontFamily = getBalooFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = amountSize,
                    letterSpacing = 0.01.em,
                    color = amountColor,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                    style = TextStyle(shadow = amountShadow)
                )
                Spacer(modifier = Modifier.height(height * 0.03f))
            }
        }
    }
}