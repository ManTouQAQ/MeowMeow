package me.mantou.meow.register

import me.mantou.meow.MeowMeow
import me.mantou.meow.config.data.*

object ConfigParserRegister : Register {

    override fun register() {
        MeowMeow.INSTANCE.configManager.apply {
            registerParser(ServerPingConfig.ServerPingConfigParser)
            registerParser(CreeperDisablerConfig.CreeperDisablerConfigParser)
            registerParser(AdminListConfig.AdminListConfigParser)
            registerParser(ServerLinksConfig.ServerLinksConfigParser)
        }
    }
}