package me.blitzgamer_88.worldteleport.placeholders

import me.blitzgamer_88.worldteleport.WorldTeleport
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class WorldTeleportPlaceholders(private val mainClass: WorldTeleport) : PlaceholderExpansion() {


    override fun getAuthor(): String {
        return "BlitzGamer_88"
    }

    override fun getIdentifier(): String {
        return "worldtp"
    }

    override fun getVersion(): String {
        return "0.0.5"
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(offlinePlayer: OfflinePlayer, input: String): String? {

        val locations = mainClass.savedLocations

        when {

            input.startsWith("location") -> {

                if (input == "location") {

                    val player = offlinePlayer.player ?: return ""
                    val worldName = player.world.name
                    val loc = locations[worldName] ?: return ""
                    return "${loc.x} ${loc.y} ${loc.z}"
                }

                val args = input.split("_")
                if (args.size > 2) {
                    return null
                }

                val world = Bukkit.getWorld(args[1]) ?: return ""
                val loc = locations[world.name] ?: return ""
                return "${loc.x} ${loc.y} ${loc.z}"

            }
        }
        return null
    }
}