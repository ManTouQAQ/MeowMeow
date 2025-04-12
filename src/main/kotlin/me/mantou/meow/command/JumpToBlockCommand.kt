package me.mantou.meow.command

import me.mantou.meow.message.ConstantMessage
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JumpToBlockCommand : Command("/j"){
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        if (!sender.hasPermission("meow.admin")) {
            sender.sendMessage(ConstantMessage.NO_PERMISSION)
            return true
        }

        val targetBlock = sender.getTargetBlockExact(128)
        if (targetBlock == null) {
            sender.sendMessage("§c未找到可站立的方块")
            return true
        }

        val location = findSafeLocation(targetBlock.location).apply {
            yaw = sender.location.yaw
            pitch = sender.location.pitch
            x += 0.5
            z += 0.5
        }

        sender.teleport(location)
        sender.sendMessage("§ePop~")
        return true
    }

    private fun findSafeLocation(startLoc: Location): Location {
        val world = startLoc.world!!
        var y = startLoc.blockY + 1

        val checkLimit = world.maxHeight - y + 1
        var check = 0

        while (check < checkLimit) {
            val footBlock = world.getBlockAt(startLoc.blockX, y, startLoc.blockZ)
            val headBlock = world.getBlockAt(startLoc.blockX, y + 1, startLoc.blockZ)
            val groundBlock = world.getBlockAt(startLoc.blockX, y - 1, startLoc.blockZ)
            if (!footBlock.type.isOccluding && !headBlock.type.isOccluding && groundBlock.type.isSolid) {
                return Location(world, startLoc.x, y.toDouble(), startLoc.z)
            }
            y++
            check++
        }
        throw RuntimeException("can't find safe location? normally unreachable here")
    }
}