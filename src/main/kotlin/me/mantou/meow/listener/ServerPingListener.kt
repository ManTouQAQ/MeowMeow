package me.mantou.meow.listener

import me.mantou.meow.config
import me.mantou.meow.config.data.ServerPingConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerPingListener : Listener{
    @EventHandler
    fun onPing(event: ServerListPingEvent){
        val config = config<ServerPingConfig>()
        event.motd = config.motdList.randomOrNull() ?: ""

        config.iconList.randomOrNull()?.apply {
            event.setServerIcon(this)
        }
    }
}