package me.mantou.meow.placer.history

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.joml.Vector3i

class RegionHistory {
    private val regionSnapshots = mutableListOf<RegionSnapshot>()
    private var pointer = -1

    private var tempWorld: World? = null
    private var tempBlockSnapshots = mutableSetOf<BlockSnapshot>()

    fun start(world: World) {
        if (tempWorld != null) throw RuntimeException("after start history must call push method")
        tempWorld = world
    }

    fun addSnapshot(x: Int, y: Int, z: Int, from: Material, to: Material, fromData: BlockData): BlockSnapshot {
        if (tempWorld == null) throw RuntimeException("after add snapshot must call start method")
        val snapshot = BlockSnapshot(Vector3i(x, y, z), from, to, fromData)
        tempBlockSnapshots.add(snapshot)
        return snapshot
    }

    fun push() {
        addRegionSnapshot(RegionSnapshot(tempWorld!!, tempBlockSnapshots.toSet()))

        tempWorld = null
        tempBlockSnapshots.clear()
    }

    private fun addRegionSnapshot(snapshot: RegionSnapshot) {
        if (pointer < regionSnapshots.lastIndex) {
            regionSnapshots.subList(pointer + 1, regionSnapshots.size).clear()
        }
        regionSnapshots.add(snapshot)
        pointer++
    }

    fun undo(): RegionSnapshot? {
        if (pointer < 0) return null
        val snapshot = regionSnapshots[pointer]
        pointer--
        return snapshot
    }

    fun redo(): RegionSnapshot? {
        if (pointer + 1 >= regionSnapshots.size) return null
        pointer++
        return regionSnapshots[pointer]
    }
}