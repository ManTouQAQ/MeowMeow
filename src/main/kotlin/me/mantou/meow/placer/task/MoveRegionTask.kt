package me.mantou.meow.placer.task

import me.mantou.meow.placer.RegionTask
import me.mantou.meow.placer.history.BlockSnapshot
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Location
import org.bukkit.Material
import org.joml.Vector3i

class MoveRegionTask(
    private val pos1: Location,
    private val pos2: Location,
    private val length: Int,
    private val direction: Vector3i
) : RegionTask {
    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)

        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)

        val needMoveBlocks = mutableListOf<BlockSnapshot>()
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    needMoveBlocks.add(history.addSnapshot(x, y, z, block.type, Material.AIR, block.blockData))
                    block.setType(Material.AIR, false)
                }
            }
        }

        val offset = Vector3i(direction).mul(length)

        for (needMoveSnapshot in needMoveBlocks) {
            val newPos = Vector3i(needMoveSnapshot.pos).add(offset)

            val block = pos1.world!!.getBlockAt(newPos.x, newPos.y, newPos.z)

            val originalSnapshot = needMoveBlocks.find { it.pos == newPos }
            if (originalSnapshot != null){
                history.addSnapshot(newPos.x, newPos.y, newPos.z, originalSnapshot.from, needMoveSnapshot.from, originalSnapshot.fromData, needMoveSnapshot.fromData)
            }else{
                history.addSnapshot(newPos.x, newPos.y, newPos.z, block.type, needMoveSnapshot.from, block.blockData, needMoveSnapshot.fromData)
            }

            block.setType(needMoveSnapshot.from, false)

            needMoveSnapshot.fromData?.apply {
                block.setBlockData(this, false)
            }
        }

        history.push()
    }
}