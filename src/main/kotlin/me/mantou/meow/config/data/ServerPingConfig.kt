package me.mantou.meow.config.data

import me.mantou.meow.config.ConfigParser
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.CachedServerIcon
import java.io.File

class ServerPingConfig(val motdList: List<String>, val iconList: List<CachedServerIcon>) {
    object ServerPingConfigParser : ConfigParser<ServerPingConfig>() {
        override fun target(): Class<ServerPingConfig> {
            return ServerPingConfig::class.java
        }

        override fun parseData(folder: File): ServerPingConfig {
            val motds: List<String>

            val motdFile = folder.resolve("./motd/motd.yml")
            if (motdFile.exists()){
                val motdConfig = YamlConfiguration.loadConfiguration(motdFile)
                motds = motdConfig.getStringList("motds")
            }else{
                motds = listOf()
            }

            val icons = mutableListOf<CachedServerIcon>()
            val iconsFolder = folder.resolve("./motd/icons")
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