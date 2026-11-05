package com.tom.romcom.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun MonthDayCell(
    day: Long,
    dotColors: List<Long>,
    isToday: Boolean,
    isSelected: Boolean,
    isPast: Boolean,
    isInShownMonth: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(52.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, shape = CircleShape)
                .border(width = 1.5.dp, if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = LocalDate.ofEpochDay(day).dayOfMonth.toString(),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isToday -> MaterialTheme.colorScheme.primary
                    !isInShownMonth -> (MaterialTheme.colorScheme.onSurfaceVariant).copy(alpha = 0.4f)
                    isPast -> MaterialTheme.colorScheme.onSurfaceVariant
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.height(6.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            dotColors.take(4).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(color = (if (!isInShownMonth || isPast) Color(color).copy(alpha = 0.4f) else Color(color)), shape = CircleShape)
                )
            }
        }
    }
}
