package me.mantou.meow.placer.history

import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.joml.Vector3i

// TODO 实现自定义数据
data class BlockSnapshot(val pos: Vector3i, val from: Material, val to: Material, val fromData: BlockData) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockSnapshot

        return pos == other.pos
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }
}