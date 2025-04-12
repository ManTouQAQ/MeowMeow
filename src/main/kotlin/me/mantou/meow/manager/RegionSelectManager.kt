package me.mantou.meow.manager

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class RegionSelectManager(private val plugin: Plugin) : Listener {
    private val playerSelectingMap = mutableMapOf<UUID, MutablePair<Location?, Location?>>()
    private val inEditingPlayers = mutableListOf<UUID>()
    private lateinit var actionBarTimer: BukkitTask

    fun init() {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        actionBarTimer = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            for (uuid in inEditingPlayers) {
                val player = Bukkit.getPlayer(uuid) ?: continue
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent("In Editing")
                        .apply { color = ChatColor.GREEN }
                )
            }
        }, 0L, 20L)
    }

    fun enableEdit(uuid: UUID) {
        inEditingPlayers.add(uuid)
    }

    fun disableEdit(uuid: UUID) {
        inEditingPlayers.remove(uuid)
        removeSelected(uuid)
    }

    fun isInEditing(uuid: UUID): Boolean {
        return inEditingPlayers.contains(uuid)
    }

    fun getSelected(uuid: UUID): Pair<Location?, Location?>? {
        val pair = playerSelectingMap[uuid] ?: return null
        return Pair(pair.left, pair.right)
    }

    fun removeSelected(uuid: UUID) {
        playerSelectingMap.remove(uuid)
    }

    fun checkSelected(uuid: UUID): Boolean {
        val selected = getSelected(uuid) ?: return false
        if (selected.first == null || selected.second == null) return false
        if (selected.first!!.world == null || selected.second!!.world == null) return false
        if (selected.first!!.world != selected.second!!.world) return false
        return true
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        playerSelectingMap.remove(event.player.uniqueId)
        inEditingPlayers.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onSelect(event: PlayerInteractEvent) {
        val uuid = event.player.uniqueId
        if (!isInEditing(uuid)) return
        if (!event.hasBlock()) return

        if (event.isBlockInHand && event.player.inventory.itemInMainHand.type == Material.WOODEN_AXE) {
            event.isCancelled = true
            return
        }

        if (event.item?.type.let { it != Material.WOODEN_AXE }) return

        val action = event.action

        val selected = playerSelectingMap.getOrPut(uuid) { MutablePair(null, null) }

        if (action == Action.LEFT_CLICK_BLOCK) {
            event.isCancelled = true
            selected.left = event.clickedBlock!!.location
            event.player.sendMessage("§eSelected Pos1")
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            event.isCancelled = true
            selected.right = event.clickedBlock!!.location
            event.player.sendMessage("§eSelected Pos2")
        }
    }
//
//    @EventHandler
//    fun onPlaceBlock(event: BlockPlaceEvent){
//
//    }
}