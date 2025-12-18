package com.billioapp.features.home.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import com.billioapp.features.home.presentation.HomeColors

@Composable
fun SmartUpdateDialog(
    visible: Boolean,
    title: String,
    currentAmountText: String,
    aiAmountText: String,
    sourceText: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
        text = {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Mevcut Tutar: $currentAmountText")
                Text(text = "Güncel Fiyat (AI): $aiAmountText", color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                if (!sourceText.isNullOrBlank()) {
                    Text(text = sourceText, color = Color.Gray, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = HomeColors.Primary)) {
                Text(text = "Güncelle", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline)) {
                Text(text = "Vazgeç", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}
