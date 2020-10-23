package me.blitzgamer_88.worldteleport.util

import ch.jalu.configme.SettingsManager
import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.conf.WorldTeleportConfiguration

var conf = null as? SettingsManager?

fun loadConfig(plugin: WorldTeleport) {
    val file = plugin.dataFolder.resolve("config.yml")

    if (!file.exists())
    {
        file.parentFile.mkdirs()
        file.createNewFile()
    }

    conf = WorldTeleportConfiguration(file)
}

fun conf(): SettingsManager
{
    return checkNotNull(conf)
}
