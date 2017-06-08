package org.horiga.study.pad.service

import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import mu.KLogging
import okhttp3.OkHttpClient
import org.horiga.study.pad.config.MyApplicationProperties
import org.horiga.study.pad.dto.Event
import org.horiga.study.pad.dto.EventItem
import org.horiga.study.pad.repository.EventRepository
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

open class NoticeService(
        val properties: MyApplicationProperties,
        val httpclient: OkHttpClient,
        val eventRepository: EventRepository) {

    companion object : KLogging()

    val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder().setNameFormat("watcher").build())

    var dailyMessage: String? = ""

    fun start() {
        scheduler.scheduleAtFixedRate({
            try {
                if (eventRepository.event != null) {

                    logger.info { "Starting event notification task" }

                    if (eventRepository.event!!.message != dailyMessage) {
                        logger.info { "Daily notify!! $dailyMessage != ${eventRepository.event!!.message}" }
                        sendDailyNotify(eventRepository.event as Event)
                        dailyMessage = eventRepository.event!!.message
                    } else {
                        logger.info { "Already notify daily event list" }
                    }

                    val events = eventRepository.event!!.events
                    for (event in events) {
                        logger.info { "EVENT = ${event.readableMessage()}" }
                        // ゲリライベント開始時間経過が1分以内であれば通知する
                    }
                } else {
                    logger.info { "eventRepository.event is null" }
                }
            } catch (e: Exception) {
                logger.error { "Failed to notification scheduler ${e.message}, $e" }
            }

        }, 10, 30, TimeUnit.SECONDS)
    }

    fun shutdown() {
        MoreExecutors.shutdownAndAwaitTermination(scheduler, 10L, TimeUnit.SECONDS)
    }

    fun sendNotify(eventItem: EventItem) {
    }

    fun sendDailyNotify(event: Event) {

    }

    fun notify(message: String) {
    }

    fun isNotifyTime(eventItem: EventItem): Boolean {
        return true
    }
}
