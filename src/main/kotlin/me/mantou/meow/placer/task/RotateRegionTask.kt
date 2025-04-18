package me.mantou.meow.placer.task

import me.mantou.meow.placer.RegionTask
import me.mantou.meow.placer.history.BlockSnapshot
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Location
import org.bukkit.Material
import org.joml.Matrix3f
import org.joml.Vector3f
import org.joml.Vector3i

class RotateRegionTask(
    private val pos1: Location,
    private val pos2: Location,
    private val center: Location,
    private val angdeg: Double,
    private val axis: Vector3i
) : RegionTask {
    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)

        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)

        val needRotateBlocks = mutableListOf<BlockSnapshot>()
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    needRotateBlocks.add(history.addSnapshot(x, y, z, block.type, Material.AIR, block.blockData))
                    block.setType(Material.AIR, false)
                }
            }
        }

        val model = Matrix3f()
        model.rotate(-Math.toRadians(angdeg).toFloat(), Vector3f(axis))

        val overlapBuffer = mutableSetOf(*needRotateBlocks.toTypedArray())
        for (needMoveSnapshot in needRotateBlocks) {
            val relativePos = Vector3f(
                needMoveSnapshot.pos.x.toFloat() - center.blockX,
                needMoveSnapshot.pos.y.toFloat() - center.blockY,
                needMoveSnapshot.pos.z.toFloat() - center.blockZ
            )

            relativePos.mul(model)

            val newX = center.blockX + Math.round(relativePos.x)
            val newY = center.blockY + Math.round(relativePos.y)
            val newZ = center.blockZ + Math.round(relativePos.z)

            val block = pos1.world!!.getBlockAt(newX, newY, newZ)
            val newPos = Vector3i(newX, newY, newZ)

            // TODO 这段太狗屎了 用来处理多个方块旋转到一起的情况
            val overlapped = overlapBuffer.find { it.pos == newPos }
            val snapshot = if (overlapped != null) {
                history.addSnapshot(
                    newX,
                    newY,
                    newZ,
                    overlapped.from,
                    needMoveSnapshot.from,
                    overlapped.fromData,
                    needMoveSnapshot.fromData
                )
            } else {
                history.addSnapshot(
                    newX,
                    newY,
                    newZ,
                    block.type,
                    needMoveSnapshot.from,
                    block.blockData,
                    needMoveSnapshot.fromData
                )
            }
            overlapBuffer.add(snapshot)

            block.setType(needMoveSnapshot.from, false)
            needMoveSnapshot.fromData?.apply {
                block.setBlockData(this, false)
            }
        }

        history.push()
    }
}