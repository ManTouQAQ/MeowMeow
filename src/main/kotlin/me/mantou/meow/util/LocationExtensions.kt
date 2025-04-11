package me.mantou.meow.util

import org.bukkit.Location

fun Location.rangeTo(other: Location): Triple<IntRange, IntRange, IntRange> {
    require(world == other.world) { "Both locations must belong to the same world" }

    val (minY, maxY) = world!!.minHeight to world!!.maxHeight - 1

    return Triple(
        minOf(blockX, other.blockX)..maxOf(blockX, other.blockX),
        minOf(blockY, other.blockY).coerceAtLeast(minY)..maxOf(blockY, other.blockY).coerceAtMost(maxY),
        minOf(blockZ, other.blockZ)..maxOf(blockZ, other.blockZ)
    )
}