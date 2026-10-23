package com.tom.romcom.calendarimport


import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class IcsEvent(
    val uid: String,
    val summary: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val location: String = ""
) {
    val seriesId: String
        get() = uid.take(8)
}

fun parseIcs(text: String): List<IcsEvent> {
    val unfolded = text.replace("\r\n", "\n").replace("\n ", "")

    val events = mutableListOf<IcsEvent>()
    var current: MutableMap<String, String>? = null
    for (rawLine in unfolded.split("\n")) {
        val line = rawLine.trim()
        if (line == "BEGIN:VEVENT") {
            current = mutableMapOf()
        } else if (line == "END:VEVENT") {
            val c = current ?: continue
            current = null

            val startTime = c["DTSTART"] ?: continue
            val endTime = c["DTEND"] ?: continue
            events.add(IcsEvent(
                uid = c["UID"] ?: continue,
                summary = c["SUMMARY"] ?: continue,
                location = c["LOCATION"] ?: "",
                start = runCatching { parseIcsTime(startTime) }.getOrNull() ?: continue,
                end = runCatching { parseIcsTime(endTime) }.getOrNull() ?: continue
            ))
        } else if (current != null && ":" in line) {
            val (key, value) = line.split(":", limit = 2)
            val cleanKey = key.substringBefore(";")
            current[cleanKey] = value.replace("\\,", ",")
        }
    }
    return events
}

private val ICS_TIME = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")

fun parseIcsTime(raw: String): ZonedDateTime {

    val local = LocalDateTime.parse(raw, ICS_TIME)
    val utc = local.atZone(ZoneOffset.UTC)
    val zurich = utc.withZoneSameInstant(ZoneId.of("Europe/Zurich"))
    return zurich
}
