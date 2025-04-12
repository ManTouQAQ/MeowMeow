package me.mantou.meow.placer.history

import org.bukkit.World

data class RegionSnapshot(val world: World, val blocks: Set<BlockSnapshot>) {
}