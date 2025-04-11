package me.mantou.meow.placer

import me.mantou.meow.placer.history.RegionHistory

// TODO 使用访问者模式, 目前不需要 因为BlockPlaceManager#queueTask是直接运行的 没有分成单元运行
interface BlockTask {
    fun accept(history: RegionHistory)
}