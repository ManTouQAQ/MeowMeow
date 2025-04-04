package me.mantou.meow

import me.mantou.meow.register.CommandRegister
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.java.JavaPlugin

class MeowMeow : JavaPlugin() {
    private val commandMap: CommandMap = Bukkit.getServer().javaClass
        .getDeclaredMethod("getCommandMap")
        .apply { isAccessible = true }
        .invoke(Bukkit.getServer()) as CommandMap

    override fun onEnable() {
        commandMap.registerAll("meow", CommandRegister.commands)
    }

    override fun onDisable() {
        unregisterCommands()
    }

    private fun unregisterCommands() {
        val knownCommands = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            .apply { isAccessible = true }
            .get(commandMap) as MutableMap<String, Command>

        CommandRegister.commands.forEach{
            knownCommands.remove(it.name)
            knownCommands.remove("meow:${it.name}")
        }
    }
}