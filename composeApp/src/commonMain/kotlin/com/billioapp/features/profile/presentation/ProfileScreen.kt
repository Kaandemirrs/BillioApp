package com.billioapp.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.core.navigation.HomeRoute
import com.billioapp.core.navigation.AiRoute
import com.billioapp.core.navigation.ProfileRoute
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.features.home.presentation.HomeSampleModels
import com.billioapp.features.home.presentation.components.BottomNavBar
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinInject(),
    onNavigateToLogin: () -> Unit = {}
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateToLogin -> onNavigateToLogin()
                is ProfileEffect.ShowError -> {
                    // Optionally show a snackbar or toast
                }
                is ProfileEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        containerColor = HomeColors.Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavBar(
                items = HomeSampleModels.bottomNav,
                selectedItemId = "profile",
                onItemSelected = { item ->
                    when (item.id) {
                        "home" -> navigator.replaceAll(HomeRoute())
                        "tracker" -> navigator.replaceAll(AiRoute())
                        "profile" -> navigator.replaceAll(ProfileRoute())
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(HomeColors.Background)
                .padding(paddingValues)
                .padding(horizontal = HomeSpacing.ScreenPadding, vertical = HomeSpacing.SectionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeSpacing.SectionSpacing)
        ) {
        // Header - top left
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = getBalooFontFamily()),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = HomeColors.TextPrimary
            )
        }

        // Avatar + Name + Email
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Avatar",
                tint = HomeColors.TextPrimary,
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.Transparent, CircleShape)
            )
            Text(
                text = "Kaan",
                style = MaterialTheme.typography.titleMedium.copy(fontFamily = getBalooFontFamily()),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = HomeColors.TextPrimary
            )
            Text(
                text = "kaandmr.kaan@hotmail.com",
                style = MaterialTheme.typography.bodyMedium,
                color = HomeColors.TextSecondary
            )
        }

        // Badges row: Pro, 17 Subscription, Verified (centered on page)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HomeSpacing.ItemSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BadgeChip(text = "Pro", background = Color(0xFF62C546))
                BadgeChip(text = "17 Subscription", background = Color(0xFFE24C4C))
                BadgeChip(text = "Verified", background = Color(0xFF4CE2D5))
            }
        }

        // Settings list
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsRow(title = "Profil Düzenle", leading = Icons.Filled.Edit, onClick = { viewModel.onEvent(ProfileEvent.EditProfileClicked) })
            Divider(color = Color.Black, thickness = 0.5.dp)
            SettingsRow(title = "Premium", leading = Icons.Filled.Star, onClick = { /* TODO: Navigate to premium */ })
            Divider(color = Color.Black, thickness = 0.5.dp)
            SettingsRow(title = "Bildirimler", leading = Icons.Filled.Notifications, onClick = { /* TODO: Navigate to notifications */ })
            Divider(color = Color.Black, thickness = 0.5.dp)
            SettingsRow(title = "Yardım & Destek", leading = Icons.Filled.HelpOutline, onClick = { /* TODO: Navigate to help */ })
            Divider(color = Color.Black, thickness = 0.5.dp)
            SettingsRow(title = "Gizlilik Politikası", leading = Icons.Filled.Security, onClick = { /* TODO: Navigate to privacy policy */ })
            Divider(color = Color.Black, thickness = 0.5.dp)
            SettingsRow(title = "Çıkış Yap", leading = Icons.Filled.Logout, onClick = { viewModel.onEvent(ProfileEvent.LogoutClicked) })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Version text
        Text(
            text = "Version 1.1",
            style = MaterialTheme.typography.bodySmall,
            color = HomeColors.TextSecondary
        )
        }
    }
}

@Composable
private fun BadgeChip(text: String, background: Color) {
    Surface(
        color = background,
        shape = RoundedCornerShape(HomeSpacing.CardCornerRadius)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun SettingsRow(title: String, leading: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = leading,
            contentDescription = null,
            tint = HomeColors.TextPrimary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = getBalooFontFamily()),
            color = HomeColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = HomeColors.TextPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}