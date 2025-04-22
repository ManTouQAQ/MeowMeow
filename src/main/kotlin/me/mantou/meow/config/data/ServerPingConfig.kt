package me.mantou.meow.config.data

import me.mantou.meow.config.ConfigParser
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.CachedServerIcon
import java.io.File

data class ServerPingConfig(val motdList: List<String>, val iconList: List<CachedServerIcon>) {
    object ServerPingConfigParser : ConfigParser<ServerPingConfig>() {
        override fun target(): Class<ServerPingConfig> {
            return ServerPingConfig::class.java
        }

        override fun parseData(folder: File): ServerPingConfig {
            val motdFile = folder.resolve("./motd/motd.yml")

            val motds = if (motdFile.exists()) {
                val motdConfig = YamlConfiguration.loadConfiguration(motdFile)
                motdConfig.getStringList("motds")
            } else {
                emptyList()
            }

            val iconsFolder = folder.resolve("./motd/icons")

            val icons = mutableListOf<CachedServerIcon>()
            if (iconsFolder.exists()) {
                iconsFolder.listFiles()!!.filter {
                    it.extension.lowercase() == "png"
                }.forEach {
                    val icon = Bukkit.getServer().loadServerIcon(it)
                    icons.add(icon)
                }
            }
            return ServerPingConfig(motds, icons)
        }
    }
}