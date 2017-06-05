package org.horiga.study.pad.service

import com.google.common.base.Splitter
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import mu.KLogging
import org.horiga.study.pad.config.MyApplicationProperties
import org.horiga.study.pad.dto.Event
import org.jsoup.Jsoup
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

open class EventFetcher(
        val properties: MyApplicationProperties) {

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

    fun update(data: List<Event>) {
        logger.info { "Starting update!!" }
    }
}

class FetchJob(
        val parent: EventFetcher,
        val properties: MyApplicationProperties): Runnable {

    companion object: KLogging()

    override fun run() {
        logger.info { "Starting fetch new events!!" }

        fetchEvents()

        // TODO

    }

    fun fetchEvents(): List<Event> {

        val groups = listOf<String>("A", "B", "C", "D", "E")
        val events = mutableListOf<Event>()

        val doc = Jsoup.connect(properties.masterEndpoint).get()
        val eventDay = doc.select("div.article p").first()

        logger.info { "[div.article p] event.day = ${eventDay.text()}" }

        val titles = doc.select("div.article h4")
        for (i in 0..titles.size - 1) {
            val element = titles[i]
            logger.info { "element($i).text = ${element.text()}" }
        }

        for (i in 0..titles.size - 1 ) {
            val tbody = doc.select("div.article tbody")[i]
            val tr = tbody.children().select("tr")
            for (j in 0..tr.size-1) {
                logger.info { "tr($j) text = ${tr[j].text()}" }
                val times = mutableListOf<String>()
                times.addAll(Splitter.on(" ").splitToList(tr[j].text()))
                times.mapIndexedTo(events) { index, t -> Event(titles[i].text(), groups[index], listOf(t)) }
            }
        }

        return events
    }
}

