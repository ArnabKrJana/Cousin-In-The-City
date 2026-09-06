package com.oneforth.cousininthecitybackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class CousinInTheCityBackendApplication

fun main(args: Array<String>) {
    runApplication<CousinInTheCityBackendApplication>(*args)
}
