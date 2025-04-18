package me.mantou.meow

import me.mantou.meow.config.ConfigManager
import me.mantou.meow.placer.RegionPlaceManager
import me.mantou.meow.manager.RegionSelectManager
import me.mantou.meow.register.CommandRegister
import me.mantou.meow.register.ConfigParserRegister
import me.mantou.meow.register.ListenerRegister
import org.bukkit.plugin.java.JavaPlugin

class MeowMeow : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: MeowMeow private set
    }

    val configManager = ConfigManager(dataFolder)
    val regionSelectManager = RegionSelectManager(this)
    val regionPlaceManager = RegionPlaceManager(this)

    init {
        INSTANCE = this
    }

    override fun onEnable() {
        ConfigParserRegister.register()
        // TODO
        //  全部实现接口 Manager 然后统一管理并初始化
        //  提供拓展方法 manager<xxxManager>() 和 manager(xxxManager::class.java)
        configManager.init()
        regionSelectManager.init()
        regionPlaceManager.init()
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