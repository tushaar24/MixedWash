package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.core.presentation.util.formatHour
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot

@Composable
fun TimeSlotGrid(
    modifier: Modifier = Modifier,
    dateSlot: DateSlot?,
    selectedTimeId: Int?,
    onSlotSelected: (TimeSlot) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.animateContentSize()
    ) {
        if (dateSlot != null) {
            val timeSlots = dateSlot.timeSlots
            var index = 0

            while (index < timeSlots.size) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    repeat(2) { offset ->
                        val actualIndex = index + offset
                        if (actualIndex >= timeSlots.size) {
                            Spacer(Modifier.weight(1f))
                            return@Row
                        }
                        val timeSlot = timeSlots[index + offset]
                        val (startTime, startSuffix) = timeSlot.startTimeStamp.formatHour()
                        val (endTime, endSuffix) = timeSlot.endTimeStamp.formatHour()


                        TimeSlotButton(
                            modifier = Modifier.weight(1f),
                            startTime = startTime,
                            startTimeSuffix = startSuffix,
                            endTime = endTime,
                            endTimeSuffix = endSuffix,
                            selected = timeSlot.id == selectedTimeId,
                            unavailable = !timeSlot.isAvailable,
                            caption = when {
                                timeSlot.offersAvailable.isNotEmpty() -> "${timeSlot.offersAvailable.size} offer${if (timeSlot.offersAvailable.size > 1) "s" else ""}"
                                else -> null
                            },
                            onClick = { onSlotSelected(timeSlot) })
                    }
                }
                index += 2
            }

        } else SlotsEmpty()
    }

}
