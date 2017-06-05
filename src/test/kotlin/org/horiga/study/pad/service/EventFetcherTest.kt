package org.horiga.study.pad.service

import mu.KLogging
import org.horiga.study.pad.config.MyApplicationProperties
import org.horiga.study.pad.dto.Event
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class EventFetcherTest {

    companion object: KLogging()

    @Autowired lateinit var eventFetcher: EventFetcher
    @Autowired lateinit var properties: MyApplicationProperties

    @Test fun runTest() {
        logger.info { "runTest" }
        val data = listOf<Event>()
        eventFetcher.update(data)
    }

    @Test fun fetchTest() {
        val job = FetchJob(eventFetcher, properties)
        val events = job.fetchEvents()
        for (e in events) {
            logger.info { "event = $e" }
        }
    }
}