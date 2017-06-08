package org.horiga.study.pad.repository

import mu.KLogging
import org.horiga.study.pad.dto.Event
import org.springframework.stereotype.Repository

@Repository
open class EventRepository {

    companion object: KLogging()

    var event: Event? = null
    fun setUp(event: Event) {
        logger.info { "setUp events" }
        this.event = event
    }

}