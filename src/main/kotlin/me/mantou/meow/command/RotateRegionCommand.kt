package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import me.mantou.meow.placer.task.RotateRegionTask
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.joml.Vector3i

class RotateRegionCommand: Command("/rotate") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        if (args.isEmpty()){
            sender.sendMessage("§cUsage: /$commandLabel <angdeg> [axis]")
            return true
        }

        val angdeg = args[0].toIntOrNull() ?: run{
            sender.sendMessage("§c角度必须为整数")
            return true
        }

        val axis = if (args.size > 1) {
            when (args[1].lowercase()) {
                "x" -> Vector3i(1, 0, 0)
                "y" -> Vector3i(0, 1, 0)
                "z" -> Vector3i(0, 0, 1)
                else -> {
                    sender.sendMessage("§c未知轴向")
                    return true
                }
            }
        } else {
            Vector3i(0, 1, 0)
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
            "§aRotate region: " +
                    "(${pos1.x}, ${pos1.y}, ${pos1.z}) " +
                    "-> " +
                    "(${pos2.x}, ${pos2.y}, ${pos2.z}) with axis (${axis.x}, ${axis.y}, ${axis.z}), angdeg $angdeg"
        )

        MeowMeow.INSTANCE
            .regionPlaceManager
            .queueTask(
                sender.uniqueId,
                RotateRegionTask(pos1, pos2, sender.location, angdeg.toDouble(), axis)
            )
        return true
    }
}