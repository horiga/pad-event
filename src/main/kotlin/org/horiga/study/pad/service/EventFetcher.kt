package org.horiga.study.pad.service

import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import mu.KLogging
import org.horiga.study.pad.config.MyApplicationProperties
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

    fun update(data: Map<String, Map<String, String>>) {}
}

class FetchJob(
        val parent: EventFetcher,
        val properties: MyApplicationProperties): Runnable {

    companion object: KLogging()

    override fun run() {
        logger.info { "Starting fetch new events!!" }
        val doc = Jsoup.connect(properties.masterEndpoint).get()
        val h4Elm = doc.select("div.article h4")
        logger.info { "html .article h4 => " +
                "element.size = ${h4Elm.size}, " +
                "element.text() = ${h4Elm.text()}"  }

        // TODO

    }
}

