package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import org.jetbrains.compose.resources.painterResource
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ampl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPriceFinderSheet(
    onDismiss: () -> Unit,
    viewModel: com.billioapp.features.home.presentation.HomeViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = HomeColors.Card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeSpacing.ScreenPadding, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Başlık ortada
            Text(
                text = "AI Fiyat Bulucu",
                color = HomeColors.Primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Üst satır: solda label + input, sağda Analiz Et
            var subscriptionName by remember { mutableStateOf("") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Abonelik adı giriniz",
                        color = HomeColors.Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    OutlinedTextField(
                        value = subscriptionName,
                        onValueChange = { subscriptionName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text(text = "örn. Netflix", color = HomeColors.TextPrimary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HomeColors.Primary,
                            unfocusedBorderColor = HomeColors.Primary,
                            cursorColor = HomeColors.Primary
                        )
                    )
                }

                Button(
                    onClick = { viewModel.onEvent(com.billioapp.features.home.presentation.HomeEvent.OnAiPriceSuggestClicked(subscriptionName)) },
                    colors = ButtonDefaults.buttonColors(containerColor = HomeColors.Primary)
                ) {
                    Text(text = "Analiz Et", color = Color.White)
                }
            }

            // Öneri alanı: büyük beyaz radiuslu kutu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Öneri: 149.90 TL",
                        color = HomeColors.Primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

            // En altta sol köşe: ampl.png + uyarı
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(Res.drawable.ampl),
                            contentDescription = null,
                            modifier = Modifier.height(20.dp)
                        )
                        Text(
                            text = "AI önerisidir doğruluğunu kontrol edin",
                            color = HomeColors.TextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}