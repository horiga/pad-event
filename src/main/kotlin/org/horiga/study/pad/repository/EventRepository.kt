package org.horiga.study.pad.repository

import jetbrains.exodus.env.Environment
import org.horiga.study.pad.dto.Event

open class EventRepository(
        val env: Environment
) {

    fun getEvents(): List<Event> {
        // TODO
        return listOf(
                Event("Tan", "A", listOf("8", "15:30")),
                Event("Tan", "B", listOf("8", "15:30")))
    }

    fun getEvent(group: String): Event? {
        return null
    }

    fun putEvent(event: Event) {
    }

    fun shutdown() {
        env.close()
    }

}