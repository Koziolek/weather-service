package pl.koziolekweb.weather.weatherservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Timestamp
import java.time.LocalDateTime

/**
 * Created by koziolek on 08.06.17.
 */
@RestController
open class WttrClient(@Value("\${app.weather-service.storage}") val storage:String = ".") {

    @RequestMapping("/{city}")
    fun getWeatherFor(@PathVariable(name = "city", required = false) location: String): String {
        val rt = RestTemplate()
        val resp = rt.getForEntity("http://wttr.in/$location", String::class.java)
        return resp.body
    }

    @RequestMapping("/{city}.png")
    fun getWeatherForAsPng(@PathVariable(name = "city", required = true) location: String): String {
        val rt = RestTemplate()
        rt.messageConverters.add(ByteArrayHttpMessageConverter())
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_OCTET_STREAM)
        val entity = HttpEntity<String>(headers)

        val resp:ResponseEntity<ByteArray> = rt.exchange("http://wttr.in/$location.png", HttpMethod.GET, entity, ByteArray::class.java)

        when (resp.statusCode) {
            HttpStatus.OK -> Files.write(Paths.get("$storage/$location-${Timestamp.valueOf(LocalDateTime.now()).time}.png"), resp.body)
            else -> print("Exit code is ${resp.statusCode}")
        }

        return resp.statusCode.toString()
    }
}