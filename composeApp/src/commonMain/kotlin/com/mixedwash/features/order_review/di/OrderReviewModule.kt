package com.mixedwash.features.order_review.di

import com.mixedwash.features.order_review.presentation.OrderReviewScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OrderReviewModule = module {
    viewModelOf(::OrderReviewScreenViewModel)
}