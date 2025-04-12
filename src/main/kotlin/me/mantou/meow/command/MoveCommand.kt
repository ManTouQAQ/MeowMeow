package me.mantou.meow.command

import me.mantou.meow.MeowMeow
import me.mantou.meow.message.ConstantMessage
import me.mantou.meow.placer.task.MoveBlockTask
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.joml.Vector3i

class MoveCommand : Command("/move") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /$commandLabel <length> [direction]")
            return true
        }

        val length = (args[0].toIntOrNull() ?: apply {
            sender.sendMessage("§c长度必须为整数")
            return true
        }) as Int

        val direction = if (args.size >= 2) {
            when (args[1].lowercase()) {
                "north" -> Vector3i(0, 0, -1)
                "south" -> Vector3i(0, 0, 1)
                "east" -> Vector3i(1, 0, 0)
                "west" -> Vector3i(-1, 0, 0)
                "up" -> Vector3i(0, 1, 0)
                "down" -> Vector3i(0, -1, 0)
                "front" -> getYawDirection(sender)
                "back" -> getYawDirection(sender, 180)
                "left" -> getYawDirection(sender, -90)
                "right" -> getYawDirection(sender, 90)
                else -> {
                    sender.sendMessage("§c未知方向: ${args[1]}")
                    return true
                }
            }
        } else {
            getDirection(sender)
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
            "§aMoving region: " +
                    "(${pos1.x}, ${pos1.y}, ${pos1.z}) " +
                    "-> " +
                    "(${pos2.x}, ${pos2.y}, ${pos2.z}) to (${direction.x}, ${direction.y}, ${direction.z}) * $length"
        )

        MeowMeow.INSTANCE
            .blockPlaceManager
            .queueTask(
                sender.uniqueId,
                MoveBlockTask(pos1, pos2, length, direction)
            )
        return true
    }

    private fun getDirection(player: Player): Vector3i {
        if (player.location.pitch < -60) return Vector3i(0, 1, 0)
        if (player.location.pitch > 60) return Vector3i(0, -1, 0)

        return getYawDirection(player)
    }

    private fun getYawDirection(player: Player, offset: Int = 0): Vector3i{
        val normalizedYaw = ((player.location.yaw + offset) % 360 + 360) % 360

        return when (normalizedYaw) {
            in 45.0..135.0 -> Vector3i(-1, 0, 0)
            in 135.0..225.0 -> Vector3i(0, 0, -1)
            in 225.0..315.0 -> Vector3i(1, 0, 0)
            else -> Vector3i(0, 0, 1)
        }
    }
}