package me.mantou.meow.config.data

import me.mantou.meow.config.ConfigParser
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class CreeperDisablerConfig(val enable: Boolean) {
    object CreeperDisablerConfigParser : ConfigParser<CreeperDisablerConfig>() {
        override fun target(): Class<CreeperDisablerConfig> {
            return CreeperDisablerConfig::class.java
        }

        override fun parseData(folder: File): CreeperDisablerConfig {
            val configFile = folder.resolve("./config.yml")

            val enable = if (configFile.exists()) YamlConfiguration.loadConfiguration(configFile)
                .getBoolean("creeper-disabler") else false
            return CreeperDisablerConfig(enable)
        }
    }
}