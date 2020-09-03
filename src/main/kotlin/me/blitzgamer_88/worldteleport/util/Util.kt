package me.blitzgamer_88.worldteleport.util

import org.bukkit.ChatColor

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)