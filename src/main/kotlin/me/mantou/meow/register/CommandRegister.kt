package me.mantou.meow.register

import me.mantou.meow.command.FillPlusCommand
import me.mantou.meow.command.GetPosCommand

object CommandRegister {
    val commands = listOf(
        FillPlusCommand(),
        GetPosCommand(),
    )
}