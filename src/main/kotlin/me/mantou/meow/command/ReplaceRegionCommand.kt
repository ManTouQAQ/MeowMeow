package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import me.mantou.meow.placer.task.ReplaceRegionTask
import me.mantou.meow.util.NMSUtil
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReplaceRegionCommand : Command("/replace") {
    override fun execute(sender: CommandSender, root: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        if (args.size < 2) {
            sender.sendMessage("§cUsage: /$root <from> <to>")
            return true
        }

        val from = parseBlockType(sender, args[0])
        val to = parseBlockType(sender, args[1])
        if (from == null || to == null) return true

        val selectManager = MeowMeow.INSTANCE.regionSelectManager

        if (!selectManager.checkSelected(sender.uniqueId)) {
            sender.sendMessage("§c请先选择区域")
            return true
        }

        val selected = MeowMeow.INSTANCE.regionSelectManager.getSelected(sender.uniqueId)!!

        val pos1 = selected.first!!
        val pos2 = selected.second!!

        sender.sendMessage("§aReplacing region: ${NMSUtil.bukkitBlockTypeToKey(from)} in " +
                "(${pos1.x}, ${pos1.y}, ${pos1.z}) " +
                "-> " +
                "(${pos2.x}, ${pos2.y}, ${pos2.z}) to ${NMSUtil.bukkitBlockTypeToKey(to)}")

        MeowMeow.INSTANCE
            .regionPlaceManager
            .queueTask(
                sender.uniqueId,
                ReplaceRegionTask(pos1, pos2, from, to)
            )
        return true
    }

    private fun parseBlockType(player: Player, blockTypeStr: String): Material? {
        if (blockTypeStr.equals("hand", true)) {
            return getInHandBlockType(player) ?: let { player.sendMessage("§c请在手中选择方块"); null }
        } else if(blockTypeStr.equals("offhand", true)){
            return getInOffHandBlockType(player) ?: let { player.sendMessage("§c请在副手中选择方块"); null }
        } else {
            val keyToBukkit = NMSUtil.blockKeyToBukkit(blockTypeStr)
            if (keyToBukkit == null) {
                player.sendMessage("§c未知方块 ($blockTypeStr)")
                return null
            }
            return keyToBukkit
        }
    }

    private fun getInHandBlockType(player: Player): Material? {
        return if (player.inventory.itemInMainHand.type.isBlock) {
            player.inventory.itemInMainHand.type
        } else {
            null
        }
    }

    private fun getInOffHandBlockType(player: Player): Material? {
        return if (player.inventory.itemInOffHand.type.isBlock) {
            player.inventory.itemInOffHand.type
        } else {
            null
        }
    }
}