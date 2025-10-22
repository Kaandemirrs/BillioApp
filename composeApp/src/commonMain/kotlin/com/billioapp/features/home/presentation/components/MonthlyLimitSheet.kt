package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyLimitSheet(
    onDismiss: () -> Unit,
    onSave: (Double) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var limitText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = HomeColors.Card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HomeColors.Card)
                .padding(horizontal = HomeSpacing.ScreenPadding)
                .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aylık Harcama Limiti",
                color = HomeColors.Primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = limitText,
                onValueChange = { new ->
                    // Sadece rakam ve nokta/virgül kabul edelim
                    val filtered = new.filter { it.isDigit() || it == '.' || it == ',' }
                    limitText = filtered
                },
                modifier = Modifier
                    .fillMaxWidth(0.72f),
                shape = RoundedCornerShape(36.dp),
                label = { Text("Aylık Limit", color = HomeColors.Primary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HomeColors.Primary,
                    unfocusedBorderColor = HomeColors.Primary,
                    focusedLabelColor = HomeColors.Primary,
                    cursorColor = HomeColors.Primary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val normalized = limitText.replace(',', '.')
                    val value = normalized.toDoubleOrNull()
                    if (value != null) {
                        onSave(value)
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HomeColors.Primary,
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            ) {
                Text(text = "Kaydet", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}