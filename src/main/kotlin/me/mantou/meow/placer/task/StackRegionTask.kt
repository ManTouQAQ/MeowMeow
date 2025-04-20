package me.mantou.meow.placer.task

import me.mantou.meow.placer.RegionTask
import me.mantou.meow.placer.history.BlockSnapshot
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Location
import org.bukkit.Material
import org.joml.Vector3i

class StackRegionTask(
    private val pos1: Location,
    private val pos2: Location,
    private val count: UInt,
    private val direction: Vector3i
) : RegionTask {
    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)

        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)

        val needStackBlocks = mutableListOf<BlockSnapshot>()
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    needStackBlocks.add(history.addSnapshot(x, y, z, block.type, Material.AIR, block.blockData))
                }
            }
        }

        val stepLength = when {
            direction.x == 0 && direction.y == 0 -> zRange.last - zRange.first
            direction.x == 0 && direction.z == 0 -> yRange.last - yRange.first
            direction.y == 0 && direction.z == 0 -> xRange.last - xRange.first
            else -> throw RuntimeException("Invalid direction: $direction")
        } + 1

        repeat(count.toInt()) { i ->
            for (needMoveSnapshot in needStackBlocks) {
                val offset = Vector3i(direction).mul(stepLength * (i + 1))
                val newPos = Vector3i(needMoveSnapshot.pos).add(offset)

                val blockAt = pos1.world!!.getBlockAt(newPos.x, newPos.y, newPos.z)

                history.addSnapshot(blockAt.x, blockAt.y, blockAt.z, blockAt.type, needMoveSnapshot.from, blockAt.blockData, needMoveSnapshot.fromData)
                blockAt.setType(needMoveSnapshot.from, false)
                needMoveSnapshot.fromData?.run {
                    blockAt.setBlockData(this, false)
                }
            }
        }
        history.push()
    }
}