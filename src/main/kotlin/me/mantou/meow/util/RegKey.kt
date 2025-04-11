package me.mantou.meow.util

data class RegKey(val namespace: String, val location: String) {
    override fun toString(): String {
        return "$namespace:$location"
    }
}