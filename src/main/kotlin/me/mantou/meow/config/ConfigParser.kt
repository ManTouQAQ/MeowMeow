package me.mantou.meow.config

import java.io.File

abstract class ConfigParser<T> {
    abstract fun target(): Class<T>

    abstract fun parseData(folder: File): T
}