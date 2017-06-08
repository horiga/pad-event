package org.horiga.study.pad.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.horiga.study.pad.repository.EventRepository
import org.horiga.study.pad.service.EventFetcher
import org.horiga.study.pad.service.NoticeService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(MyApplicationProperties::class)
@Configuration
open class MyApplicationConfig(
        val properties: MyApplicationProperties,
        val eventRepository: EventRepository) {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModules(KotlinModule(), AfterburnerModule(), JavaTimeModule())
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }

    @Bean
    fun okHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(properties.http.connectTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
                    .readTimeout(properties.http.readTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
                    .followRedirects(true)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build()

    @Bean(destroyMethod = "shutdown")
    fun noticeService(): NoticeService {
        val noticeService = NoticeService(properties, okHttpClient(), eventRepository)
        noticeService.start()
        return noticeService
    }

    @Bean(destroyMethod = "shutdown")
    fun eventFetcher(): EventFetcher {
        val eventFetcher = EventFetcher(properties, eventRepository)
        eventFetcher.start()
        return eventFetcher
    }
}