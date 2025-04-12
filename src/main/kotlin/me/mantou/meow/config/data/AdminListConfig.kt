package me.mantou.meow.config.data

import me.mantou.meow.config.ConfigParser
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.UUID

data class AdminListConfig(val adminList: List<UUID>, val adminPerms: List<String>) {
    object AdminListConfigParser : ConfigParser<AdminListConfig>() {
        override fun target(): Class<AdminListConfig> {
            return AdminListConfig::class.java
        }

        override fun parseData(folder: File): AdminListConfig {
            val configFile = folder.resolve("./config.yml")
            val config = if (configFile.exists()) YamlConfiguration.loadConfiguration(configFile) else null
            val adminList = config?.getStringList("admins") ?: emptyList()
            val adminPerms = config?.getStringList("admin-perms") ?: emptyList()

            return AdminListConfig(adminList.map { UUID.fromString(it) }, adminPerms)
        }
    }
}