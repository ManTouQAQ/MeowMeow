package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import me.mantou.meow.placer.task.FillBlockTask
import me.mantou.meow.util.NMSUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetBlockCommand : Command("/set") {
    override fun execute(sender: CommandSender, root: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        val blockType = if (args.isEmpty() && sender.inventory.itemInMainHand.type.isBlock) {
            sender.inventory.itemInMainHand.type
        } else {
            if (args.isEmpty()) {
                sender.sendMessage("§c请在手中选择放置的方块或指定方块注册名")
                return true
            }
            val keyToBukkit = NMSUtil.blockKeyToBukkit(args[0])
            if (keyToBukkit == null) {
                sender.sendMessage("§c未知方块 (${args[0]})")
                return true
            }
            keyToBukkit
        }

        val selectManager = MeowMeow.INSTANCE.regionSelectManager

        if (!selectManager.checkSelected(sender.uniqueId)) {
            sender.sendMessage("§c请先选择区域")
            return true
        }

        val selected = MeowMeow.INSTANCE.regionSelectManager.getSelected(sender.uniqueId)!!

        val pos1 = selected.first!!
        val pos2 = selected.second!!

        sender.sendMessage("§aFilling region: " +
                "(${pos1.x}, ${pos1.y}, ${pos1.z}) " +
                "-> " +
                "(${pos2.x}, ${pos2.y}, ${pos2.z}) to ${NMSUtil.bukkitBlockTypeToKey(blockType)}")

        MeowMeow.INSTANCE
            .blockPlaceManager
            .queueTask(
                sender.uniqueId,
                FillBlockTask(pos1, pos2, blockType)
            )
        return true
    }
}