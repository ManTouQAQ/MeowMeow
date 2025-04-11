package me.mantou.meow.command

import me.mantou.meow.message.ConstantMessage
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GetPosCommand : Command("getpos") {
    override fun execute(sender: CommandSender, root: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConstantMessage.ONLY_PLAYER)
            return true
        }

        val loc = sender.location
        val x = loc.blockX
        val y = loc.blockY
        val z = loc.blockZ

        // Create the clickable coordinate text
        val textComponent = TextComponent("§e你当前的位置为: §f$x, $y, $z")
        textComponent.clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "$x $y $z")
        textComponent.hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            Text("§7点击复制坐标")
        )

        sender.spigot().sendMessage(textComponent)
        return true
    }
}