package pl.koziolekweb.weather.weatherservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class WeatherServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(WeatherServiceApplication::class.java, *args)
}
