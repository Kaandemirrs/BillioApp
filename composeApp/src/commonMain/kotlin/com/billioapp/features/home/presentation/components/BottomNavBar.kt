package com.billioapp.features.home.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import com.billioapp.features.home.presentation.BottomNavItemModel
import com.billioapp.features.home.presentation.HomeColors
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavBar(
    items: List<BottomNavItemModel>,
    modifier: Modifier = Modifier,
    selectedItemId: String? = null,
    onItemSelected: (BottomNavItemModel) -> Unit = {}
) {
    var internalSelectedItemId by remember(items) { mutableStateOf(items.firstOrNull()?.id) }
    val effectiveSelectedId = selectedItemId ?: internalSelectedItemId

    NavigationBar(
        modifier = modifier.height(60.dp),
        containerColor = HomeColors.Card
    ) {
        items.forEachIndexed { index, item ->
            // bar daraltıldı; ikonlar: sol=26dp, orta=30dp, sağ=24dp
            val iconSize = when (index) {
                1 -> 32.dp
                2 -> 26.dp
                else -> 28.dp
            }
            NavigationBarItem(
                selected = item.id == effectiveSelectedId,
                onClick = {
                    if (selectedItemId == null) {
                        internalSelectedItemId = item.id
                    }
                    onItemSelected(item)
                },
                modifier = Modifier.height(60.dp),
                icon = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = item.contentDescription,
                            modifier = Modifier.size(iconSize),
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = HomeColors.Background,
                    selectedIconColor = HomeColors.Primary,
                    unselectedIconColor = HomeColors.TextSecondary
                )
            )
        }
    }
}