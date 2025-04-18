package me.mantou.meow.register

import me.mantou.meow.command.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap

object CommandRegister : Register {
    private const val COMMAND_NAMESPACE = "meow"
    private val commandMap: CommandMap = Bukkit.getServer().javaClass
        .getDeclaredMethod("getCommandMap")
        .apply { isAccessible = true }
        .invoke(Bukkit.getServer()) as CommandMap

    private val commands = listOf(
        SetRegionCommand(),
        GetPosCommand(),
        MeowCommand(),
        HatCommand(),
        ReplaceRegionCommand(),
        UndoCommand(),
        RedoCommand(),
        MoveRegionCommand(),
        FlipRegionCommand(),
        JumpToBlockCommand(),
        RotateRegionCommand(),
        UpToBlockCommand(),
        StackRegionCommand(),
    )

    override fun register() {
        commandMap.registerAll(COMMAND_NAMESPACE, commands)
    }

    @Suppress("UNCHECKED_CAST")
    override fun unregister() {
        val knownCommands = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            .apply { isAccessible = true }
            .get(commandMap) as MutableMap<String, Command>

        commands.forEach {
            it.unregister(commandMap)
            knownCommands.remove(it.name)
            knownCommands.remove("meow:${it.name}")
        }
    }
}