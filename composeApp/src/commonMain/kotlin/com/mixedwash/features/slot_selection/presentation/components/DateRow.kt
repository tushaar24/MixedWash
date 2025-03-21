package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.core.presentation.util.getDayAndDate
import com.mixedwash.core.presentation.util.getMonth
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot

@Composable
fun DateRow(slots: List<DateSlot>, onClick: (DateSlot) -> Unit, selectedDateId: Int? = null) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val datesByMonth = slots.groupBy { it.timeStamp.getMonth() }
        datesByMonth.forEach { (month, dates) ->
            item {
                Month(month = month)
            }
            items(dates.size, key = { index -> dates[index].timeStamp }) { index ->
                val dateSlot = dates[index]
                val (day, date) = dateSlot.timeStamp.getDayAndDate()
                DateSlotButton(day = day,
                    date = date,
                    selected = dateSlot.id == selectedDateId,
                    disabled = !dateSlot.isAvailable(),
                    onClick = { onClick(dateSlot) }
                )
            }
        }
    }
}