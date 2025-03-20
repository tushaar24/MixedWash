package com.mixedwash.features.getting_started.model

import androidx.compose.ui.graphics.Color

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageUrl: String,
    val backgroundColor: Color
)

val onboardingItems = listOf(
    OnboardingItem(
        title = "Free Pickup & Delivery",
        description = "We offer free pickup and delivery, so you can relax while we take care of everything.",
        imageUrl = "https://assets-aac.pages.dev/assets/delivery_scooter.png",
        backgroundColor = Color(0xFFFEE6CD)
    ),
    
    OnboardingItem(
        title = "Hassle Free Process",
        description = "Enjoy easy pickup with no need to count your clothes. Convenience at its best, with no extra charges.",
        imageUrl = "https://assets-aac.pages.dev/assets/basket_woman.png",
        backgroundColor = Color(0xFFF1F0CC)
    ),

    OnboardingItem(
        title = "Doorstep Weighing",
        description = "Your laundry is weighed at your doorstep, ensuring complete transparency. We confirm the price on the spot.",
        imageUrl = "https://assets-aac.pages.dev/assets/weighing_scale.png",
        backgroundColor = Color(0xFFCFE2A8)
    ),

    OnboardingItem(
        title = "Delivery in 24 hrs",
        description = "We value your time. Fresh, clean clothes delivered to your doorstep within 24 hours.",
        imageUrl = "https://assets-aac.pages.dev/assets/man_relaxing.png",
        backgroundColor = Color(0xFFC9C9C9)
    )
)
