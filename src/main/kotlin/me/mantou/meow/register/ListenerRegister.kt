package me.mantou.meow.register

import me.mantou.meow.MeowMeow
import me.mantou.meow.listener.*
import org.bukkit.Bukkit

object ListenerRegister : Register {

    private val listeners = listOf(
        ServerPingListener(),
        CreeperDisabler(),
        AdminJoinListener(),
    )

    override fun register() {
        listeners.forEach {
            Bukkit.getServer().pluginManager.registerEvents(it, MeowMeow.INSTANCE)
        }
    }
}