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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.material3.CircularProgressIndicator
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import com.billioapp.features.home.presentation.AddSubscriptionViewModel
import com.billioapp.features.home.presentation.AddSubscriptionEvent
import com.billioapp.features.home.presentation.AddSubscriptionEffect
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

enum class BillingCycle { DAILY, WEEKLY, MONTHLY, YEARLY }

data class AddBillData(
    val name: String,
    val category: String,
    val amount: Double,
    val cycle: BillingCycle,
    val paymentDay: Int?,
    val color: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillSheet(
    onDismiss: () -> Unit,
    onSave: (AddBillData) -> Unit = {},
    onAiPriceFinderRequested: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val viewModel: AddSubscriptionViewModel = koinInject()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddSubscriptionEffect.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(effect.message) }
            }
        }
    }

    var name by remember { mutableStateOf("") }
    // Backend'e gönderilecek kategori anahtarı (ör. entertainment, utilities)
    var category by remember { mutableStateOf("") }
    // UI'da gösterilecek etiket (Türkçe)
    var categoryLabel by remember { mutableStateOf("") }
    val amount = uiState.amount
    var paymentDay by remember { mutableStateOf("") }
    var cycle by remember { mutableStateOf(BillingCycle.MONTHLY) }
    var unknownPaymentDay by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    // bllio.pdf mimarisi: UI etiketleri → backend anahtarları
    // Örnekler: Market → other, Medikal → health, Abonelikler → entertainment, Ulaşım → utilities
    val categoryOptions = listOf(
        "Eğlence" to "entertainment",
        "Ulaşım" to "utilities",
        "Medikal" to "health",
        "Abonelikler" to "entertainment",
        "Diğer" to "other"
    )

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
                value = uiState.name,
                onValueChange = {
                    name = it
                    viewModel.onEvent(AddSubscriptionEvent.NameChanged(it))
                },
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

            // Kategori (Dropdown)
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                val displayValue = categoryOptions.firstOrNull { it.second == category }?.first ?: categoryLabel
                OutlinedTextField(
                    value = displayValue,
                    onValueChange = { input ->
                        // Serbest giriş: kullanıcı Türkçe etiket yazarsa, backend anahtarına map et
                        val trimmed = input.trim()
                        categoryLabel = trimmed
                        val mapped = categoryOptions.firstOrNull { it.first.equals(trimmed, ignoreCase = true) }?.second
                        category = mapped ?: "other"
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .align(Alignment.CenterHorizontally)
                        .menuAnchor(),
                    shape = RoundedCornerShape(36.dp),
                    label = { Text("Kategori:", color = HomeColors.Primary) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomeColors.Primary,
                        unfocusedBorderColor = HomeColors.Primary,
                        focusedLabelColor = HomeColors.Primary,
                        cursorColor = HomeColors.Primary
                    )
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        color = HomeColors.Card,
                    ) {
                        Column(
                            modifier = Modifier
                                .border(1.dp, HomeColors.Primary, RoundedCornerShape(16.dp))
                                .padding(vertical = 4.dp)
                        ) {
                            categoryOptions.forEach { (display, code) ->
                                val selected = code == category
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (selected) HomeColors.Primary.copy(alpha = 0.08f) else Color.Transparent
                                        )
                                        .clickable {
                                            // Dropdown'dan seçim: hem label hem backend anahtarı set edilir
                                            categoryLabel = display
                                            category = code
                                            categoryExpanded = false
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = display,
                                        color = HomeColors.TextPrimary,
                                        fontSize = 15.sp
                                    )
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = HomeColors.Primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Aylık Tutar
            var selectedColor by remember { mutableStateOf<String?>(null) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }
                        viewModel.onEvent(AddSubscriptionEvent.AmountChanged(filtered))
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.72f),
                    shape = RoundedCornerShape(36.dp),
                    label = { Text("Aylık Tutar:", color = HomeColors.Primary) },
                    trailingIcon = {
                        if (uiState.isAiLoading) {
                            CircularProgressIndicator(
                                color = HomeColors.Primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(HomeColors.Card)
                                    .clickable { viewModel.onEvent(AddSubscriptionEvent.AskAiForPrice) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = "AI Fiyat Doldur",
                                    tint = HomeColors.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
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

                Box(
                    modifier = Modifier
                        .background(HomeColors.Card, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "TL", color = HomeColors.Primary, fontSize = 14.sp)
                }
            }

            if (!uiState.aiSource.isNullOrBlank()) {
                Text(
                    text = uiState.aiSource ?: "",
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp
                )
            } else if (!uiState.aiError.isNullOrBlank()) {
                Text(
                    text = uiState.aiError ?: "",
                    color = Color(0xFFC62828),
                    fontSize = 12.sp
                )
            }

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

            // Renk Seçim Paleti
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Renk Seçimi:", color = HomeColors.Primary, fontSize = 16.sp)
                ColorPaletteRow(
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )
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
                    enabled = !unknownPaymentDay,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomeColors.Primary,
                        unfocusedBorderColor = HomeColors.Primary,
                        focusedLabelColor = HomeColors.Primary,
                        cursorColor = HomeColors.Primary
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Küçük tıklanabilir kutucuk (checkbox benzeri)
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.dp, HomeColors.Primary, RoundedCornerShape(4.dp))
                        .background(if (unknownPaymentDay) HomeColors.Primary else Color.Transparent)
                        .clickable {
                            unknownPaymentDay = !unknownPaymentDay
                            if (unknownPaymentDay) paymentDay = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (unknownPaymentDay) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bilmiyorum",
                    fontSize = 15.sp,
                    color = HomeColors.Primary,
                    modifier = Modifier.clickable {
                        unknownPaymentDay = !unknownPaymentDay
                        if (unknownPaymentDay) paymentDay = ""
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Kaydet
            Button(
                onClick = {
                    val amt = uiState.amount.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val day = paymentDay.toIntOrNull()
                    onSave(
                        AddBillData(
                            name = uiState.name,
                            category = category,
                            amount = amt,
                            cycle = cycle,
                            paymentDay = day,
                            color = selectedColor
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
