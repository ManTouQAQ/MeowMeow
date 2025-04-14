package me.mantou.meow.placer.task

import me.mantou.meow.placer.BlockTask
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Location
import org.bukkit.Material

// TODO 将范围抽象出来 比如 Cube Sphere
class FillBlockTask(private val pos1: Location, private val pos2: Location, private val type: Material) :
    BlockTask {
    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)
        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    history.addSnapshot(x, y, z, block.type, type, block.blockData)
                    block.setType(type, false)
                }
            }
        }
        history.push()
    }
}