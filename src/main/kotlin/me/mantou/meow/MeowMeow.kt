package me.mantou.meow

import me.mantou.meow.config.ConfigManager
import me.mantou.meow.register.CommandRegister
import me.mantou.meow.register.ConfigParserRegister
import me.mantou.meow.register.ListenerRegister
import org.bukkit.plugin.java.JavaPlugin

class MeowMeow : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: MeowMeow private set
    }

    val configManager = ConfigManager(dataFolder)

    init {
        INSTANCE = this
    }

    override fun onEnable() {
        ConfigParserRegister.register()
        configManager.init()
        CommandRegister.register()
        ListenerRegister.register()
    }

    override fun onDisable() {
        CommandRegister.unregister()
        ListenerRegister.unregister()
    }
}

fun <T : Any> config(clazz: Class<T>): T {
    return MeowMeow.INSTANCE.configManager.get(clazz)
}

inline fun <reified T : Any> config(): T {
    return MeowMeow.INSTANCE.configManager.get(T::class.java)
}