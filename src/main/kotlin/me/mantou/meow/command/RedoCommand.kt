package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RedoCommand : Command("/redo"){
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        val result = MeowMeow.INSTANCE.blockPlaceManager.redoHistory(sender.uniqueId)

        if (result){
            sender.sendMessage("§a成功恢复")
        }else{
            sender.sendMessage("§c当前无历史")
        }
        return true
    }
}