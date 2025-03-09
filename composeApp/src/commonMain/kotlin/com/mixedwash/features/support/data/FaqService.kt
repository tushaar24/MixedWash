package com.mixedwash.features.support.data

import com.mixedwash.features.support.domain.model.FaqItemDTO
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemTagDto

private val faqData = listOf(
    FaqItemDTO(
        "How do I manage all the notifications on the App?",
        "In the Profile section, go to Notification Settings. Here, you can turn all notifications on or off collectively. You can also customize notification for specific categories, such as Orders, Purchases, Promotions, and Offers, based on your preferences.",
        listOf(FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I check my booked tickets on the district app?",
        "After booking, you'll receive all the ticket details via Whatsapp and email. You can also view your booking by going to the movie tickets section in your profile on the district app",
        listOf(FaqItemCategoryDto.All, FaqItemCategoryDto.Pickup)
    ),
    FaqItemDTO(
        "Where can I set my event tickets?",
        "Head to your profile on the app and find event tickets to view all your event bookings.",
        listOf(FaqItemCategoryDto.General, FaqItemCategoryDto.All, FaqItemCategoryDto.Processing)
    ),
    FaqItemDTO(
        "How do I pay my dining bill using the District app?",
        "After dining at a partner restaurant, simply open the restaurant's page in the district app and use the pay bill option. Alternatively, you can scan the district qr code if available at the restaurant",
        listOf(FaqItemCategoryDto.Delivery, FaqItemCategoryDto.Pickup, FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I manage all the notifications on the App?",
        "In the Profile section, go to Notification Settings. Here, you can turn all notifications on or off collectively. You can also customize notification for specific categories, such as Orders, Purchases, Promotions, and Offers, based on your preferences.",
        listOf(FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I check my booked tickets on the district app?",
        "After booking, you'll receive all the ticket details via Whatsapp and email. You can also view your booking by going to the movie tickets section in your profile on the district app",
        listOf(FaqItemCategoryDto.All, FaqItemCategoryDto.Pickup)
    ),
    FaqItemDTO(
        "Where can I set my event tickets?",
        "Head to your profile on the app and find event tickets to view all your event bookings.",
        listOf(FaqItemCategoryDto.General, FaqItemCategoryDto.All, FaqItemCategoryDto.Processing)
    ),
    FaqItemDTO(
        "How do I pay my dining bill using the District app?",
        "After dining at a partner restaurant, simply open the restaurant's page in the district app and use the pay bill option. Alternatively, you can scan the district qr code if available at the restaurant",
        listOf(FaqItemCategoryDto.Delivery, FaqItemCategoryDto.Pickup, FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I manage all the notifications on the App?",
        "In the Profile section, go to Notification Settings. Here, you can turn all notifications on or off collectively. You can also customize notification for specific categories, such as Orders, Purchases, Promotions, and Offers, based on your preferences.",
        listOf(FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I check my booked tickets on the district app?",
        "After booking, you'll receive all the ticket details via Whatsapp and email. You can also view your booking by going to the movie tickets section in your profile on the district app",
        listOf(FaqItemCategoryDto.All, FaqItemCategoryDto.Pickup)
    ),
    FaqItemDTO(
        "Where can I set my event tickets?",
        "Head to your profile on the app and find event tickets to view all your event bookings.",
        listOf(FaqItemCategoryDto.General, FaqItemCategoryDto.All, FaqItemCategoryDto.Processing)
    ),
    FaqItemDTO(
        "How do I pay my dining bill using the District app?",
        "After dining at a partner restaurant, simply open the restaurant's page in the district app and use the pay bill option. Alternatively, you can scan the district qr code if available at the restaurant",
        listOf(FaqItemCategoryDto.Delivery, FaqItemCategoryDto.Pickup, FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I manage all the notifications on the App?",
        "In the Profile section, go to Notification Settings. Here, you can turn all notifications on or off collectively. You can also customize notification for specific categories, such as Orders, Purchases, Promotions, and Offers, based on your preferences.",
        listOf(FaqItemCategoryDto.All)
    ),
    FaqItemDTO(
        "How do I check my booked tickets on the district app?",
        "After booking, you'll receive all the ticket details via Whatsapp and email. You can also view your booking by going to the movie tickets section in your profile on the district app",
        listOf(FaqItemCategoryDto.All, FaqItemCategoryDto.Pickup)
    ),
    FaqItemDTO(
        "Where can I set my event tickets?",
        "Head to your profile on the app and find event tickets to view all your event bookings.",
        listOf(FaqItemCategoryDto.General, FaqItemCategoryDto.All, FaqItemCategoryDto.Processing)
    ),
    FaqItemDTO(
        "How do I pay my dining bill using the District app?",
        "After dining at a partner restaurant, simply open the restaurant's page in the district app and use the pay bill option. Alternatively, you can scan the district qr code if available at the restaurant",
        listOf(FaqItemCategoryDto.Delivery, FaqItemCategoryDto.Pickup, FaqItemCategoryDto.All),
        listOf(FaqItemTagDto.LOGIN_ISSUE)
    ),
)

class FaqService {

    fun getAllFaqs(): Result<List<FaqItemDTO>> {
        return Result.success(faqData)
    }

    fun getFaqsByLabel(label: FaqItemCategoryDto): Result<List<FaqItemDTO>> {
        return Result.success(faqData.filter { it.categories.contains(label) })
    }

    fun searchFaqs(searchString: String): Result<List<FaqItemDTO>> {
        // search each question, answer, and the tags for the substring
        val result = faqData.filter { faqItem ->
            faqItem.question.contains(searchString, ignoreCase = true)
                    || faqItem.answer.contains(searchString, ignoreCase = true)
                    || faqItem.tags.any { tag -> tag.name.contains(searchString, ignoreCase = true) }
        }

        return Result.success(result)
    }

    fun filterByTag(tag: String) : Result<List<FaqItemDTO>> {
        return Result.success(
            faqData.filter {
                it.tags.any { t -> t.displayTag.contains(tag, ignoreCase = true) }
            }
        )
    }
}