package org.horiga.study.pad.service

import mu.KLogging
import okhttp3.OkHttpClient
import org.horiga.study.pad.config.MyApplicationProperties

open class NoticeService(
        val properties: MyApplicationProperties,
        val httpclient: OkHttpClient) {

    companion object: KLogging()

    fun sendNotify() {}
}