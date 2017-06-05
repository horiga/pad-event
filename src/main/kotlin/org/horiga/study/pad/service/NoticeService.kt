package org.horiga.study.pad.service

import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import mu.KLogging
import okhttp3.OkHttpClient
import org.horiga.study.pad.config.MyApplicationProperties
import org.horiga.study.pad.dto.Event
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

open class NoticeService(
        val properties: MyApplicationProperties,
        val httpclient: OkHttpClient) {

    companion object: KLogging()

    val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder().setNameFormat("watcher").build())

    fun start() {
        scheduler.scheduleAtFixedRate({

            // select from eventRepository

            // event.notified == false && time is over > sendNotify

        }, 1, 5, TimeUnit.MINUTES)
    }

    fun shutdown() {
        MoreExecutors.shutdownAndAwaitTermination(scheduler, 10L, TimeUnit.SECONDS)
    }

    fun sendNotify(event: Event) {
    }
}
