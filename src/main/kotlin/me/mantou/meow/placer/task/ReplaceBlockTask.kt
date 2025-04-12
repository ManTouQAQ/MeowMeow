package me.mantou.meow.placer.task

import me.mantou.meow.placer.BlockTask
import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.util.rangeTo
import org.bukkit.Location
import org.bukkit.Material

class ReplaceBlockTask(
    private val pos1: Location,
    private val pos2: Location,
    private val from: Material,
    private val to: Material
) : BlockTask {
    override fun accept(history: RegionHistory) {
        history.start(pos1.world!!)
        val (xRange, yRange, zRange) = pos1.rangeTo(pos2)
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    val block = pos1.world!!.getBlockAt(x, y, z)
                    if (block.type == from) {
                        history.addSnapshot(x, y, z, block.type, to, block.blockData)
                        block.type = to
                    }
                }
            }
        }
        history.push()
    }
}