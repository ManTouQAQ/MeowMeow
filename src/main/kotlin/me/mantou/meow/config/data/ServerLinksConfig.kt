package me.mantou.meow.config.data

import me.mantou.meow.config.ConfigParser
import org.bukkit.configuration.file.YamlConfiguration
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI

data class ServerLinksConfig(val links: Map<String, URI>) {
    object ServerLinksConfigParser : ConfigParser<ServerLinksConfig>() {
        private val logger = LoggerFactory.getLogger(ServerLinksConfigParser::class.java)

        override fun target(): Class<ServerLinksConfig> {
            return ServerLinksConfig::class.java
        }

        override fun parseData(folder: File): ServerLinksConfig {
            val linksFile = folder.resolve("./links/links.yml")

            val links = if (linksFile.exists()) {
                val linksConfig = YamlConfiguration.loadConfiguration(linksFile)
                val map = mutableMapOf<String, URI>()

                for (key in linksConfig.getKeys(false)) {
                    val value = linksConfig.getString(key)!!

                    map[key] = try {
                        URI.create(value)
                    } catch (_: IllegalArgumentException) {
                        logger.warn("Invalid URI for key '$key': $value")
                        continue
                    }
                }

                map
            } else {
                emptyMap()
            }
            return ServerLinksConfig(links)
        }
    }
}