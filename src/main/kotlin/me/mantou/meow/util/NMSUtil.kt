package me.mantou.meow.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.MinecraftKey
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_21_R4.block.CraftBlockType

class NMSUtil {
    companion object{
        fun blockKeyToBukkit(key: String): Material? {
            val minecraftKey = MinecraftKey.a(key)

            if (!BuiltInRegistries.e.d(minecraftKey)) return null

            val nmsBlock = BuiltInRegistries.e.a(minecraftKey)
            return CraftBlockType.minecraftToBukkit(nmsBlock)
        }

        fun bukkitBlockTypeToKey(material: Material): RegKey{
            val block = CraftBlockType.bukkitToMinecraft(material)
            val key = BuiltInRegistries.e.b(block)
            return RegKey(key.b(), key.a())
        }
    }
}