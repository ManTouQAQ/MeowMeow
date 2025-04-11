package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MeowCommand : Command("meow") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (args.isEmpty()){
            sender.sendMessage("§emeow~")
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        if (args.size == 1){
            if (args[0].equals("reload", true)){
                MeowMeow.INSTANCE.configManager.reloadAll()
                sender.sendMessage("§a[Meow] Config Reloaded")
                return true
            }else if (args[0].equals("edit", true)){
                if (sender !is Player){
                    sender.sendMessage(ConstantMessage.ONLY_PLAYER)
                    return true
                }

                val inEditing = MeowMeow.INSTANCE.regionSelectManager.isInEditing(sender.uniqueId)
                if (inEditing){
                    MeowMeow.INSTANCE.regionSelectManager.disableEdit(sender.uniqueId)
                }else{
                    MeowMeow.INSTANCE.regionSelectManager.enableEdit(sender.uniqueId)
                }

                sender.sendMessage("§a[Meow] ${if (!inEditing) "§aEnable" else "§cDisable"} §aEditing")
                return true
            }
        }
        return true
    }
}