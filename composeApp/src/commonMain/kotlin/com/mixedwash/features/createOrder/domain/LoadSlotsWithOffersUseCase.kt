package com.mixedwash.features.createOrder.domain

import com.mixedwash.domain.models.Resource
import com.mixedwash.features.createOrder.domain.models.SlotsAndOffersEntity
import com.mixedwash.features.createOrder.presentation.screens.DateSlot
import com.mixedwash.features.createOrder.presentation.screens.Offer
import com.mixedwash.features.createOrder.presentation.screens.TimeSlot
import kotlinx.coroutines.delay

class LoadSlotsWithOffersUseCase {
    suspend operator fun invoke(): Resource<SlotsAndOffersEntity> {
        delay(300)
        return Resource.Success(
            SlotsAndOffersEntity(
                pickupSlots = listOf(
                    // Date 1: 2025-01-15
                    DateSlot(
                        timeStamp = 1736899200L, // 2025-01-15T00:00:00 UTC
                        timeSlots = listOf(
                            TimeSlot(
                                startTimeStamp = 1736933400L, // 9:30 AM
                                endTimeStamp = 1736944200L,   // 12:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 10% OFF",
                                        subtitle = "10% off on SBI Credit Card",
                                        code = "10%OFFSBI"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736944200L, // 12:00 PM
                                endTimeStamp = 1736955000L,   // 3:00 PM
                                isAvailable = true,
                                offersAvailable = listOf()
                            ),
                            TimeSlot(
                                startTimeStamp = 1736955000L, // 3:00 PM
                                endTimeStamp = 1736965800L,   // 6:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 20% OFF",
                                        subtitle = "20% off on orders above $50",
                                        code = "20%OFF50"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736965800L, // 6:00 PM
                                endTimeStamp = 1736976600L,   // 9:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 15% OFF",
                                        subtitle = "15% off on any order",
                                        code = "15%OFF"
                                    ),
                                    Offer(
                                        title = "Buy 1 Get 1 Free",
                                        subtitle = "Applicable on select items",
                                        code = "B1G1"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736976600L, // 9:00 PM
                                endTimeStamp = 1736987400L,   // 12:00 AM
                                isAvailable = true,
                                offersAvailable = listOf()
                            )
                        )
                    ),
                    // Date 2: 2025-01-16
                    DateSlot(
                        timeStamp = 1736985600L, // 2025-01-16T00:00:00 UTC
                        timeSlots = listOf(
                            TimeSlot(
                                startTimeStamp = 1737019800L, // 9:30 AM
                                endTimeStamp = 1737030600L,   // 12:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 5% OFF",
                                        subtitle = "5% off on all orders",
                                        code = "5%OFF"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737030600L, // 12:00 PM
                                endTimeStamp = 1737041400L,   // 3:00 PM
                                isAvailable = true,
                                offersAvailable = listOf()
                            ),
                            TimeSlot(
                                startTimeStamp = 1737041400L, // 3:00 PM
                                endTimeStamp = 1737052200L,   // 6:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 15% OFF",
                                        subtitle = "15% off on orders above $100",
                                        code = "15%OFF100"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737052200L, // 6:00 PM
                                endTimeStamp = 1737063000L,   // 9:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 20% OFF",
                                        subtitle = "20% off on any order",
                                        code = "20%OFF"
                                    ),
                                    Offer(
                                        title = "Free Delivery",
                                        subtitle = "Free delivery on all orders",
                                        code = "FREEDELIVERY"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737063000L, // 9:00 PM
                                endTimeStamp = 1737073800L,   // 12:00 AM
                                isAvailable = true,
                                offersAvailable = listOf()
                            )
                        )
                    )
                ),
                dropSlots = listOf(
                    // Date 1: 2025-01-15
                    DateSlot(
                        timeStamp = 1736899200L, // 2025-01-15T00:00:00 UTC
                        timeSlots = listOf(
                            TimeSlot(
                                startTimeStamp = 1736933400L, // 9:30 AM
                                endTimeStamp = 1736944200L,   // 12:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 10% OFF",
                                        subtitle = "10% off on SBI Credit Card",
                                        code = "10%OFFSBI"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736944200L, // 12:00 PM
                                endTimeStamp = 1736955000L,   // 3:00 PM
                                isAvailable = true,
                                offersAvailable = listOf()
                            ),
                            TimeSlot(
                                startTimeStamp = 1736955000L, // 3:00 PM
                                endTimeStamp = 1736965800L,   // 6:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 20% OFF",
                                        subtitle = "20% off on orders above $50",
                                        code = "20%OFF50"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736965800L, // 6:00 PM
                                endTimeStamp = 1736976600L,   // 9:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 15% OFF",
                                        subtitle = "15% off on any order",
                                        code = "15%OFF"
                                    ),
                                    Offer(
                                        title = "Buy 1 Get 1 Free",
                                        subtitle = "Applicable on select items",
                                        code = "B1G1"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1736976600L, // 9:00 PM
                                endTimeStamp = 1736987400L,   // 12:00 AM
                                isAvailable = true,
                                offersAvailable = listOf()
                            )
                        )
                    ),
                    // Date 2: 2025-01-16
                    DateSlot(
                        timeStamp = 1736985600L, // 2025-01-16T00:00:00 UTC
                        timeSlots = listOf(
                            TimeSlot(
                                startTimeStamp = 1737019800L, // 9:30 AM
                                endTimeStamp = 1737030600L,   // 12:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 5% OFF",
                                        subtitle = "5% off on all orders",
                                        code = "5%OFF"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737030600L, // 12:00 PM
                                endTimeStamp = 1737041400L,   // 3:00 PM
                                isAvailable = true,
                                offersAvailable = listOf()
                            ),
                            TimeSlot(
                                startTimeStamp = 1737041400L, // 3:00 PM
                                endTimeStamp = 1737052200L,   // 6:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 15% OFF",
                                        subtitle = "15% off on orders above $100",
                                        code = "15%OFF100"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737052200L, // 6:00 PM
                                endTimeStamp = 1737063000L,   // 9:00 PM
                                isAvailable = true,
                                offersAvailable = listOf(
                                    Offer(
                                        title = "Flat 20% OFF",
                                        subtitle = "20% off on any order",
                                        code = "20%OFF"
                                    ),
                                    Offer(
                                        title = "Free Delivery",
                                        subtitle = "Free delivery on all orders",
                                        code = "FREEDELIVERY"
                                    )
                                )
                            ),
                            TimeSlot(
                                startTimeStamp = 1737063000L, // 9:00 PM
                                endTimeStamp = 1737073800L,   // 12:00 AM
                                isAvailable = true,
                                offersAvailable = listOf()
                            )
                        )
                    )
                ),
                commonOffers = listOf(
                    Offer(
                        title = "Flat 10% OFF",
                        subtitle = "10% off on SBI Credit Card",
                        code = "10%OFFSBI"
                    ),
                    Offer(
                        title = "100 Rs Cashback",
                        subtitle = "Up to 100Rs on orders above 500 Rs",
                        code = "100CASHBACK"
                    ),
                )
            )
        )
    }
}
