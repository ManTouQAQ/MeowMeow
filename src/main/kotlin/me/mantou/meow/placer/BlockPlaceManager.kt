package me.mantou.meow.placer

import me.mantou.meow.placer.history.RegionHistory
import me.mantou.meow.placer.history.RegionSnapshot
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.UUID

class BlockPlaceManager(private val plugin: Plugin) : Listener {
    private val placeHistories = mutableMapOf<UUID, RegionHistory>()

    fun init() {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun queueTask(uuid: UUID, task: BlockTask) {
        val history = placeHistories.getOrPut(uuid) { RegionHistory() }
        task.accept(history)
    }

    fun undoHistory(uuid: UUID): Boolean {
        val history = placeHistories[uuid] ?: return false
        val snapshot = history.undo() ?: return false
        patchSnapshot(snapshot)
        return true
    }

    fun redoHistory(uuid: UUID): Boolean {
        val history = placeHistories[uuid] ?: return false
        val snapshot = history.redo() ?: return false
        patchSnapshot(snapshot, true)
        return true
    }

    private fun patchSnapshot(snapshot: RegionSnapshot, redo: Boolean = false) {
        for (block in snapshot.blocks) {
            snapshot.world.getBlockAt(block.pos.x, block.pos.y, block.pos.z).apply {
                type = if (redo) block.to else block.from
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        placeHistories.remove(event.player.uniqueId)
    }
}