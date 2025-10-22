package com.billioapp.features.home.presentation.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_add
import com.billioapp.features.home.presentation.HomeColors
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = HomeColors.Primary,
        contentColor = Color.White
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_add),
            contentDescription = "Yeni abonelik ekle"
        )
    }
}