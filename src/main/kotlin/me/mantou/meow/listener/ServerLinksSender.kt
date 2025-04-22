package me.mantou.meow.listener

import me.mantou.meow.config
import me.mantou.meow.config.data.ServerLinksConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent

class ServerLinksSender : Listener {
    @Suppress("UnstableApiUsage")
    @EventHandler
    fun onSend(event: PlayerLinksSendEvent) {
        config<ServerLinksConfig>().links.forEach { (key, value) ->
            event.links.apply {
                addLink(key, value)
            }
        }
    }
}