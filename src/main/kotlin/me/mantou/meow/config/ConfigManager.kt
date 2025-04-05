package me.mantou.meow.config

import java.io.File

class ConfigManager(private val baseFolder: File) {

    private val configStore = mutableMapOf<Class<*>, Any>()

    private val configParserStore = mutableListOf<ConfigParser<*>>()

    fun init() {
        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
        reloadAll()
    }

    fun <T : Any> registerParser(parser: ConfigParser<T>) {
        configParserStore.add(parser)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: Class<T>): T {
        return configStore[clazz] as? T ?: throw RuntimeException("Config with type $clazz not found!")
    }

    fun reloadAll() {
        configParserStore.forEach {
            reloadConfig0(it.target(), it)
        }
    }

    fun reloadConfig(clazz: Class<*>) {
        val parser = configParserStore.find { it.target() == clazz } ?: throw RuntimeException("ConfigParser with type $clazz not found!")
        reloadConfig0(parser.target(), parser)
    }

    private fun reloadConfig0(clazz: Class<*>, parser: ConfigParser<*>){
        configStore[clazz] = parser.parseData(baseFolder) as Any
    }
}