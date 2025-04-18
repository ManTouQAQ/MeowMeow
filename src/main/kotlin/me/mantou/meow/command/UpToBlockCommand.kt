package me.mantou.meow.command

import me.mantou.meow.message.ConstantMessage
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UpToBlockCommand : Command("/up"){
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        val height = args[0].toUIntOrNull() ?: run {
            sender.sendMessage("§c高度必须为正整数")
            return true
        }

        val location = sender.location
        location.y += height.toInt()

        val blockAt = sender.world.getBlockAt(location)
        if (!blockAt.type.isAir){
            sender.sendMessage("§c目标位置有方块")
            return true
        }
        blockAt.type = Material.GLASS
        sender.teleport(location.apply {
            y = blockY + 1.0
            x = blockX + 0.5
            z = blockZ + 0.5
        })
        sender.sendMessage("§aPop~")
        return true
    }
}