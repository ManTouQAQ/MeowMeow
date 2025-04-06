package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MeowCommand : Command("meow") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (args.isEmpty()){
            sender.sendMessage("§emeow~")
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage("§cNo permissions")
            return true
        }

        if (args.size == 1 && args[0].equals("reload", true)){
            MeowMeow.INSTANCE.configManager.reloadAll()
            sender.sendMessage("§a[MEOW] Config Reloaded")
        }
        return true
    }
}