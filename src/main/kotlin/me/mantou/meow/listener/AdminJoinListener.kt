package me.mantou.meow.listener

import me.mantou.meow.MeowMeow
import me.mantou.meow.config
import me.mantou.meow.config.data.AdminListConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class AdminJoinListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerLoginEvent) {
        val config = config<AdminListConfig>()
        if (!config.adminList.contains(event.player.uniqueId)) return

        config.adminPerms.forEach {
            event.player.addAttachment(MeowMeow.INSTANCE, it, true)
        }
    }
}