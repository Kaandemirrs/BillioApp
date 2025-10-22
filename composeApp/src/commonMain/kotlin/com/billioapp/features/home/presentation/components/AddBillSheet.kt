package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing

enum class BillingCycle { DAILY, WEEKLY, MONTHLY, YEARLY }

data class AddBillData(
    val name: String,
    val category: String,
    val amount: Double,
    val cycle: BillingCycle,
    val paymentDay: Int?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillSheet(
    onDismiss: () -> Unit,
    onSave: (AddBillData) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var paymentDay by remember { mutableStateOf("") }
    var cycle by remember { mutableStateOf(BillingCycle.MONTHLY) }

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
                .padding(top = 16.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Yeni Abonelik Ekle",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HomeColors.Primary
            )

            // Abonelik Adı
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(36.dp),
                label = { Text("Abonelik Adı:", color = HomeColors.Primary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HomeColors.Primary,
                    unfocusedBorderColor = HomeColors.Primary,
                    focusedLabelColor = HomeColors.Primary,
                    cursorColor = HomeColors.Primary
                )
            )

            // Kategori
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(36.dp),
                label = { Text("Kategori:", color = HomeColors.Primary) },
                trailingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = HomeColors.Primary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HomeColors.Primary,
                    unfocusedBorderColor = HomeColors.Primary,
                    focusedLabelColor = HomeColors.Primary,
                    cursorColor = HomeColors.Primary
                )
            )

            // Aylık Tutar
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(36.dp),
                label = { Text("Aylık Tutar:", color = HomeColors.Primary) },
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .background(HomeColors.Card, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "TL", color = HomeColors.Primary, fontSize = 14.sp)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HomeColors.Primary,
                    unfocusedBorderColor = HomeColors.Primary,
                    focusedLabelColor = HomeColors.Primary,
                    cursorColor = HomeColors.Primary
                )
            )

            // Döngü: kutu tasarımında 4 seçenek

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Döngü:", color = HomeColors.Primary, fontSize = 16.sp)
                @Composable
                fun CycleOption(text: String, selected: Boolean, onClick: () -> Unit) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(2.dp, HomeColors.Primary, RoundedCornerShape(14.dp))
                            .background(if (selected) HomeColors.Primary else HomeColors.Card)
                            .clickable(onClick = onClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) Color.White else HomeColors.Primary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CycleOption("GÜNLÜK", cycle == BillingCycle.DAILY) { cycle = BillingCycle.DAILY }
                    CycleOption("HAFTA", cycle == BillingCycle.WEEKLY) { cycle = BillingCycle.WEEKLY }
                    CycleOption("AY", cycle == BillingCycle.MONTHLY) { cycle = BillingCycle.MONTHLY }
                    CycleOption("YIL", cycle == BillingCycle.YEARLY) { cycle = BillingCycle.YEARLY }
                }
            }

            // ÖDEME GÜNÜ + sağında "Bilmiyorum" metni
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = paymentDay,
                    onValueChange = { paymentDay = it.filter { ch -> ch.isDigit() } },
                    modifier = Modifier.fillMaxWidth(0.45f),
                    shape = RoundedCornerShape(36.dp),
                    label = { Text("ÖDEME GÜNÜ", color = HomeColors.Primary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomeColors.Primary,
                        unfocusedBorderColor = HomeColors.Primary,
                        focusedLabelColor = HomeColors.Primary,
                        cursorColor = HomeColors.Primary
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Bilmiyorum",
                    fontSize = 15.sp,
                    color = HomeColors.Primary,
                    modifier = Modifier.clickable { paymentDay = "" }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Kaydet
            Button(
                onClick = {
                    val amt = amount.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val day = paymentDay.toIntOrNull()
                    onSave(
                        AddBillData(
                            name = name,
                            category = category,
                            amount = amt,
                            cycle = cycle,
                            paymentDay = day
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HomeColors.Primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Kaydet", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}