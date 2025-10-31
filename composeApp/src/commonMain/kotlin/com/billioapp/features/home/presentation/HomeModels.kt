package com.billioapp.features.home.presentation

import org.jetbrains.compose.resources.DrawableResource
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_logo
import billioapp.composeapp.generated.resources.ic_money
import billioapp.composeapp.generated.resources.ic_notification
import billioapp.composeapp.generated.resources.tabMenu1
import billioapp.composeapp.generated.resources.tabMenu2
import billioapp.composeapp.generated.resources.tabMenu3

import billioapp.composeapp.generated.resources.tabmenu3

data class TrackerCategory(
    val name: String,
    val amount: Double,
    val colorHex: Long
)

data class TrackerModel(
    val totalAmount: Double,
    val currency: String,
    val categories: List<TrackerCategory>
)

data class InfoCardModel(
    val message: String,
    val iconRes: DrawableResource
)

data class BillItemModel(
    val id: String,
    val name: String,
    val amountText: String,
    val leadingColorHex: Long,
    val primaryColorHex: Long,
    val trailingColorHex: Long,
    val iconRes: DrawableResource
)

data class BottomNavItemModel(
    val id: String,
    val iconRes: DrawableResource,
    val contentDescription: String
)

object HomeSampleModels {
    val tracker = TrackerModel(
        totalAmount = 1800.0,
        currency = "TL",
        categories = listOf(
            TrackerCategory("Medikal", 620.0, 0xFF3F51B5),
            TrackerCategory("Market", 420.0, 0xFFFFC107),
            TrackerCategory("Ulaşım", 260.0, 0xFFFF5722),
            TrackerCategory("Eğlence", 220.0, 0xFF4CAF50),
            TrackerCategory("Abonelikler", 180.0, 0xFF9C27B0),
            TrackerCategory("Diğer", 100.0, 0xFF607D8B)
        )
    )

    val infoCard = InfoCardModel(
        message = "İlk aboneliğini ekle ve harcamalarını takip et!",
        iconRes = Res.drawable.ic_logo
    )

    val bills = listOf(
        BillItemModel("sample-spotify", "Spotify", "32 TL", 0xFFFFEB3B, 0xFF69F0AE, 0xFFFF5252, Res.drawable.ic_logo),
        BillItemModel("sample-netflix", "Netflix", "89 TL", 0xFFFFC107, 0xFF4CAF50, 0xFFEF5350, Res.drawable.ic_logo),
        BillItemModel("sample-icloud", "iCloud", "19 TL", 0xFFFFE082, 0xFF80CBC4, 0xFFFF8A80, Res.drawable.ic_logo)
    )

    // mevcut ikonları tabmenu1/2/3 ile değiştirildi
    val bottomNav = listOf(
        BottomNavItemModel("home", Res.drawable.tabMenu1, "Ana Sayfa"),
        BottomNavItemModel("tracker", Res.drawable.tabMenu2, "Takip"),
        BottomNavItemModel("profile", Res.drawable.tabMenu3, "Profil")
    )
}