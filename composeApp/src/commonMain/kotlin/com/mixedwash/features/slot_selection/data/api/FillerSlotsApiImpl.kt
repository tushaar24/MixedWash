package com.mixedwash.features.slot_selection.data.api

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto
import com.mixedwash.features.slot_selection.domain.api.FillerSlotsApi
import com.mixedwash.features.slot_selection.domain.model.FillerConfig
import com.mixedwash.features.slot_selection.domain.model.TimeSlotConfig
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

class FillerSlotsApiImpl : FillerSlotsApi {

    override val defaultSlotConfigs = listOf(
        TimeSlotConfig(
            startTime = LocalTime(12, 0), // 12:00 PM
            endTime = LocalTime(15, 0)    // 3:00 PM
        ),
        TimeSlotConfig(
            startTime = LocalTime(15, 0), // 3:00 PM
            endTime = LocalTime(18, 0)    // 6:00 PM
        ),
        TimeSlotConfig(
            startTime = LocalTime(18, 0), // 6:00 PM
            endTime = LocalTime(21, 0)    // 9:00 PM
        )
    )


    override fun generateSlots(config: FillerConfig): List<DateSlotDto> {
        val futureDates = (0 until config.daysToGenerate).map { daysToAdd ->
            config.startDateInclusive.plus(DatePeriod(days = daysToAdd))
        }

        return futureDates.map { date ->
            generateSlotsForDate(date, config.slotConfigs)
        }
    }

    private fun generateSlotsForDate(
        date: LocalDate,
        slotConfigs: List<TimeSlotConfig>
    ): DateSlotDto {
        val timeZone = TimeZone.currentSystemDefault()

        val timeSlots = slotConfigs.map { config ->
            val startInstant = date
                .atTime(config.startTime)
                .toInstant(timeZone)

            val endInstant = date
                .atTime(config.endTime)
                .toInstant(timeZone)

            TimeSlotDto(
                startTimeStamp = startInstant.epochSeconds,
                endTimeStamp = endInstant.epochSeconds,
                isAvailable = true
            )
        }

        return DateSlotDto(
            timeStamp = date
                .atStartOfDayIn(timeZone)
                .epochSeconds,
            timeSlots = timeSlots
        )
    }
}
