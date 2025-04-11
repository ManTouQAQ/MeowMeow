package me.mantou.meow.command

import me.mantou.meow.message.ConstantMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HatCommand : Command("hat") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player){
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        val inventory = sender.inventory
        val mainHandItem = inventory.itemInMainHand
        if (mainHandItem.type.isAir){
            sender.sendMessage("§c请将物品置于主手中~")
            return true
        }

        val helmetItem = inventory.helmet
        inventory.setItemInMainHand(helmetItem)
        inventory.helmet = mainHandItem
        sender.sendMessage("§aSucceed")
        return true
    }
}