package me.mantou.meow.placer.history

import org.bukkit.Material
import org.joml.Vector3i

// TODO 实现自定义数据
data class BlockSnapshot(val pos: Vector3i, val from: Material, val to: Material) {
}