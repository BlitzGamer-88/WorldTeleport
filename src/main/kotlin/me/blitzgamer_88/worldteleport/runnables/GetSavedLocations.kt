package me.blitzgamer_88.worldteleport.runnables

import me.blitzgamer_88.worldteleport.WorldTeleport
import org.bukkit.scheduler.BukkitRunnable

class GetSavedLocations(private val plugin: WorldTeleport) : BukkitRunnable() {
    override fun run() {
        val locations = plugin.getLocationsConfig() ?: return
        val keys = locations.getKeys(false)
        plugin.savedLocations.clear()
        for (key in keys) {
            val location = locations.getLocation(key) ?: continue
            plugin.savedLocations[key] = location
        }
    }
}