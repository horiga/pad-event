package org.horiga.study.pad.service

import com.google.common.base.Splitter
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import mu.KLogging
import org.horiga.study.pad.config.MyApplicationProperties
import org.horiga.study.pad.dto.Event
import org.horiga.study.pad.dto.EventItem
import org.horiga.study.pad.repository.EventRepository
import org.jsoup.Jsoup
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

open class EventFetcher(
        val properties: MyApplicationProperties,
        val eventRepository: EventRepository) {

    companion object: KLogging()

    val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder().setNameFormat("fetcher").build())

    fun start() {
        logger.info { "start fetcher scheduler!!" }
        val delay = properties.fetchIntervalMinutes.toLong()
        scheduler.scheduleAtFixedRate(FetchJob(this, properties), 1, delay, TimeUnit.MINUTES)
    }

    fun shutdown() {
        MoreExecutors.shutdownAndAwaitTermination(scheduler, 10L, TimeUnit.SECONDS)
    }

    fun update(event: Event) {
        eventRepository.setUp(event)
    }
}

class FetchJob(
        val parent: EventFetcher,
        val properties: MyApplicationProperties): Runnable {

    companion object: KLogging()

    override fun run() {
        logger.info { "Starting fetch guerrilla event from ${properties.masterEndpoint}" }
        parent.update(fetchDayEvent())
    }

    fun fetchDayEvent(): Event {

        val groups = listOf("A", "B", "C", "D", "E")
        val events = mutableListOf<EventItem>()

        val doc = Jsoup.connect(properties.masterEndpoint).get()
        val eventDay = doc.select("div.article p").first()
        val titles = doc.select("div.article h4")
        for (i in 0..titles.size - 1 ) {
            val tbody = doc.select("div.article tbody")[i]
            val tr = tbody.children().select("tr")
            for (j in 0..tr.size-1) {
                val times = mutableListOf<String>()
                times.addAll(Splitter.on(" ").splitToList(tr[j].text()))
                times.mapIndexedTo(events) { index, t -> EventItem(titles[i].text(), groups[index], listOf(t)) }
            }
        }

        return Event(eventDay.text(), events)
    }
}

