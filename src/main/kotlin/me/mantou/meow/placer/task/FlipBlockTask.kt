package me.mantou.meow.placer.task

import me.mantou.meow.placer.BlockTask
import me.mantou.meow.placer.history.BlockSnapshot
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Axis
import org.bukkit.Location
import org.bukkit.Material
import org.joml.Vector3i

class FlipBlockTask(
    private val pos1: Location,
    private val pos2: Location,
    private val flipAxis: Axis
) : BlockTask {

    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)
        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)

        val needFlipBlocks = mutableListOf<BlockSnapshot>()
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    needFlipBlocks.add(BlockSnapshot(Vector3i(x, y, z), block.type, Material.AIR, block.blockData))
                }
            }
        }

        val minX = xRange.first
        val maxX = xRange.last
        val minY = yRange.first
        val maxY = yRange.last
        val minZ = zRange.first
        val maxZ = zRange.last

        for (snapshot in needFlipBlocks) {
            val x = snapshot.pos.x
            val y = snapshot.pos.y
            val z = snapshot.pos.z

            val newPos = when (flipAxis) {
                Axis.Y -> Vector3i(x, maxY - (y - minY), z)
                Axis.X -> Vector3i(maxX - (x - minX), y, z)
                Axis.Z -> Vector3i(x, y, maxZ - (z - minZ))
            }

            val block = pos1.world!!.getBlockAt(newPos.x, newPos.y, newPos.z)
            history.addSnapshot(
                newPos.x,
                newPos.y,
                newPos.z,
                block.type,
                snapshot.from,
                block.blockData,
                snapshot.fromData
            )
            block.type = snapshot.from
            snapshot.fromData?.apply {
                block.blockData = this
            } // TODO 实现带有方向性的方块的转向
        }

        history.push()
    }
}