package org.horiga.study.pad

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PadEventApplication

fun main(args: Array<String>) {
    SpringApplication.run(PadEventApplication::class.java, *args)
}
