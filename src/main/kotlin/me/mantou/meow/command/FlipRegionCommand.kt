package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import me.mantou.meow.placer.task.FlipRegionTask
import org.bukkit.Axis
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlipRegionCommand : Command("/flip") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        val axis = if (args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "x" -> Axis.X
                "y" -> Axis.Y
                "z" -> Axis.Z
                else -> {
                    sender.sendMessage("§c未知轴向")
                    return true
                }
            }
        } else {
            if (sender.location.pitch < -60 || sender.location.pitch > 60) {
                Axis.Y
            }else{
                val normalizedYaw = (sender.location.yaw % 360 + 360) % 360
                when (normalizedYaw) {
                    in 45.0..135.0 -> Axis.Z
                    in 225.0..315.0 -> Axis.Z
                    in 135.0..225.0 -> Axis.X
                    else -> Axis.X
                }
            }
        }

        val selectManager = MeowMeow.INSTANCE.regionSelectManager

        if (!selectManager.checkSelected(sender.uniqueId)) {
            sender.sendMessage("§c请先选择区域")
            return true
        }

        val selected = MeowMeow.INSTANCE.regionSelectManager.getSelected(sender.uniqueId)!!

        val pos1 = selected.first!!
        val pos2 = selected.second!!

        sender.sendMessage(
            "§aFlipping region: " +
                    "(${pos1.x}, ${pos1.y}, ${pos1.z}) " +
                    "-> " +
                    "(${pos2.x}, ${pos2.y}, ${pos2.z}) axis $axis"
        )

        MeowMeow.INSTANCE
            .regionPlaceManager
            .queueTask(
                sender.uniqueId,
                FlipRegionTask(pos1, pos2, axis)
            )
        return true
    }
}