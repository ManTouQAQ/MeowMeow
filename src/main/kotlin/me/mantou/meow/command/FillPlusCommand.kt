package me.mantou.meow.command

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.MinecraftKey
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_21_R4.block.CraftBlockType
import org.bukkit.entity.Player

class FillPlusCommand : Command("fill+") {
    override fun execute(sender: CommandSender, root: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("meowmeow.admin")) {
            sender.sendMessage("§cNo permissions")
            return true
        }

        if (args.size < 7) {
            sender.sendMessage("§cUsage: /fill+ <x1> <y1> <z1> <x2> <y2> <z2> <block> [world]")
            return true
        }

        try {
            val x1 = args[0].toInt()
            val y1 = args[1].toInt()
            val z1 = args[2].toInt()
            val x2 = args[3].toInt()
            val y2 = args[4].toInt()
            val z2 = args[5].toInt()

            val world = if (sender !is Player) {
                if (args.size < 8) {
                    sender.sendMessage("§c非玩家使用必须要指定世界名称!")
                    return true
                }

                Bukkit.getWorld(args[7]) ?: run {
                    sender.sendMessage("§c未知世界")
                    return true
                }
            } else {
                sender.world
            }

            val minecraftKey = MinecraftKey.a(args[6])

            if (!BuiltInRegistries.e.d(minecraftKey)) {
                sender.sendMessage("§cUnknown block $minecraftKey")
                return true
            }

            val nmsBlock = BuiltInRegistries.e.a(minecraftKey)
            val bukkitBlock = CraftBlockType.minecraftToBukkit(nmsBlock)

            val minX = minOf(x1, x2)
            val maxX = maxOf(x1, x2)
            val minY = minOf(y1, y2).coerceAtLeast(world.minHeight)
            val maxY = maxOf(y1, y2).coerceAtMost(world.maxHeight - 1)
            val minZ = minOf(z1, z2)
            val maxZ = maxOf(z1, z2)
            fillBlocks(world, minX, minY, minZ, maxX, maxY, maxZ, bukkitBlock)

            sender.sendMessage("§aFill region: ($x1, $y1, $z1) -> ($x2, $y2, $z2) to $minecraftKey")
        } catch (e: NumberFormatException) {
            sender.sendMessage("§c坐标必须是整数！")
        }

        return true
    }

    private fun fillBlocks(
        world: World,
        minX: Int,
        minY: Int,
        minZ: Int,
        maxX: Int,
        maxY: Int,
        maxZ: Int,
        material: Material
    ) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)
                    block.type = material
                }
            }
        }
    }
}