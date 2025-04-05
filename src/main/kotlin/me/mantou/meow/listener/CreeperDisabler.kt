package me.mantou.meow.listener

import me.mantou.meow.config
import me.mantou.meow.config.data.CreeperDisablerConfig
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class CreeperDisabler : Listener {
    @EventHandler
    fun onExplosion(e: EntityExplodeEvent){
        if (e.entityType != EntityType.CREEPER) return
        if (config<CreeperDisablerConfig>().enable.not()) return
        e.blockList().clear()
    }
}