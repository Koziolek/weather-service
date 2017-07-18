package pl.koziolekweb.weather.weatherservice

import org.springframework.stereotype.Component

/**
 * Created by BKuczynski on 2017-07-06.
 */
@Component
open class WeatherConfigurationRepository {

    fun getStorageInfo(): StorageInfo {
        val reduce = System.getProperties()
                .filterKeys { k -> k.toString().startsWith(STORAGE_PREFIX) }
                .map { entry ->
                    when {
                        entry.key.toString().contains("STRG_PATH") -> StorageInfo(entry.value.toString())
                        else -> StorageInfo("")
                    }
                }
                .reduce {
                    l, r ->
                    l
                }
        return reduce
    }

    fun getServiceInfo(): ServiceInfo {
        fun pairMerge(lp: Pair<String, String>, rp: Pair<String, String>): Pair<String, String> {
            return when (lp.first) {
                "" -> Pair(rp.first, lp.second)
                else -> Pair(lp.first, rp.second)
            }
        }

        val reduce = System.getProperties()
                .filterKeys { k -> k.toString().startsWith(STORAGE_PREFIX) }
                .map { entry ->
                    when {
                        entry.key.toString().contains("WS_PATH") -> ServiceInfo(path = entry.value.toString(), port = "")
                        entry.key.toString().contains("WS_PORT") -> ServiceInfo(port = entry.value.toString(), path = "")
                        else -> ServiceInfo("", "")
                    }
                }
                .foldRight(ServiceInfo("", "")) {
                    l, r ->
                    val pair = pairMerge(Pair(l.path, l.port), Pair(r.path, r.port))
                    return ServiceInfo(pair.first, pair.second)
                }
        return reduce
    }

    fun getMode(): Mode {
        val mode = System.getProperty("APP_MODE", Mode.PROXY.toString()).toUpperCase()
        println("CURRENT CONFIGURED MODE IS: ${System.getProperties()}")
        return Mode.valueOf(mode)
    }

}

enum class Mode {
    PROXY, CLIENT
}

val SERVICE_PREFIX = "WS"
val STORAGE_PREFIX = "STRG"

data class StorageInfo(val path: String)
data class ServiceInfo(val path: String, val port: String)