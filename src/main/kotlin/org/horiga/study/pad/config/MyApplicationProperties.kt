package org.horiga.study.pad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty


@ConfigurationProperties(prefix="myapp")
open class MyApplicationProperties {
    var masterEndpoint = "http://xn--0ck4aw2hs54q8dr9xi3r6an8t.com/guerrilla-time/"
    var notificationDayOfHour = 8
    var notifyEndpoint = "https://notify-api.line.me"
    var notifyToken = ""
    val fetchIntervalMinutes = 10

    @NestedConfigurationProperty
    var http: HttpProperties = HttpProperties()

    open class HttpProperties {
        var connectTimeoutMillis = 3000
        var readTimeoutMillis = 5000
    }

}