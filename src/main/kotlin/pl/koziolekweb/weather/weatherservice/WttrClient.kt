package pl.koziolekweb.weather.weatherservice

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

/**
 * Created by koziolek on 08.06.17.
 */
@RestController
open class WttrClient {

    @RequestMapping("/{city}")
    fun getWeatherFor(@PathVariable(name = "city", required = false) location: String): String {
        val rt = RestTemplate()
        val resp = rt.getForEntity("http://wttr.in/" + location, String::class.java)
        return resp.body
    }
}