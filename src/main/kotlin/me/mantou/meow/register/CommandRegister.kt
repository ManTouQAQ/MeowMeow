package me.mantou.meow.register

import me.mantou.meow.command.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap

object CommandRegister : Register {
    private const val COMMAND_NAMESPACE = "meow"
    private val commandMap: CommandMap = Bukkit.getServer().javaClass
        .getDeclaredMethod("getCommandMap")
        .apply { isAccessible = true }
        .invoke(Bukkit.getServer()) as CommandMap

    private val commands = listOf(
        SetBlockCommand(),
        GetPosCommand(),
        MeowCommand(),
        HatCommand(),
        ReplaceBlockCommand(),
        UndoCommand(),
        RedoCommand(),
        MoveCommand(),
    )

    override fun register() {
        commandMap.registerAll(COMMAND_NAMESPACE, commands)
    }

    override fun unregister() {
        val knownCommands = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            .apply { isAccessible = true }
            .get(commandMap) as MutableMap<*, *>

        commands.forEach {
            knownCommands.remove(it.name)
            knownCommands.remove("meow:${it.name}")
        }
    }
}