package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Baloo2_Regular
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.Vector
import billioapp.composeapp.generated.resources.bilgicardicon

import com.billioapp.features.home.presentation.InfoCardModel
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoCardSection(
    model: InfoCardModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cardWidth = maxWidth
        val cardHeight = cardWidth * 0.2727f // Figma oranı: 288 / 1056
        val cornerRadius = 42.dp // Figma köşe yarıçapı

        val leftContentWidth = cardWidth * 0.2875f // Figma x konumu ~303px → sol alan oranı
        val balooFamily = FontFamily(Font(Res.font.Baloo2_Regular))

        Row(
            modifier = Modifier
                .height(cardHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color(0xFFE9F635))
                .padding(horizontal = cardWidth * 0.028f), // hafif iç boşluk
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol taraftaki iki katmanlı ikon kompozisyonu
            Box(
                modifier = Modifier
                    .width(leftContentWidth)
                    .fillMaxHeight()
            ) {
                // Büyük bilgi kartı ikonu (alt-sol)
                Image(
                    painter = painterResource(Res.drawable.bilgicardicon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(leftContentWidth * 0.54f)
                        .align(Alignment.BottomStart)
                )

                // Bulut ikonu (küçük, üst-sol, hafif offset)
                Image(
                    painter = painterResource(Res.drawable.Vector),
                    contentDescription = null,
                    modifier = Modifier
                        .size(leftContentWidth * 0.28f)
                        .align(Alignment.TopStart)
                        .offset(x = leftContentWidth * 0.06f, y = leftContentWidth * 0.02f)
                )
            }

            // Metin alanı (Baloo2 Regular, beyaz)
            Text(
                text = model.message + " 🎉",
                fontFamily = balooFamily,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .width(cardWidth - leftContentWidth)
                    .padding(start = cardWidth * 0.01f)
            )
        }
    }
}