package org.horiga.study.pad.repository

import jetbrains.exodus.env.Environment
import org.horiga.study.pad.dto.Event

open class EventRepository(
        val env: Environment // TODO: change to persistent
) {
    var events : List<Event> = listOf()
}