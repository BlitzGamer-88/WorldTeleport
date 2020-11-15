package me.blitzgamer_88.worldteleport

import me.blitzgamer_88.worldteleport.cmd.CommandWorldTeleport
import me.blitzgamer_88.worldteleport.conf.Config
import me.blitzgamer_88.worldteleport.placeholders.WorldTeleportPlaceholders
import me.blitzgamer_88.worldteleport.runnables.GetSavedLocations
import me.blitzgamer_88.worldteleport.util.conf
import me.blitzgamer_88.worldteleport.util.loadConfig
import me.blitzgamer_88.worldteleport.util.log
import me.bristermitten.pdm.PDMBuilder
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Level


class WorldTeleport : JavaPlugin() {

    var savedLocations = HashMap<String, Location>()

    private var locations: FileConfiguration? = null
    private var locationsFile: File? = null

    fun reloadLocationsConfig() {
        if (locationsFile == null) locationsFile = File(dataFolder, "locations.yml")
        locations = YamlConfiguration.loadConfiguration(locationsFile!!)
    }

    fun saveLocationsConfig() {
        if (locations == null || locationsFile == null) return
        try { getLocationsConfig()!!.save(locationsFile!!) }
        catch (ex: IOException) { logger.log(Level.SEVERE, "Could not save config to $locationsFile", ex) }
    }

    fun getLocationsConfig(): FileConfiguration? {
        if (locations == null) reloadLocationsConfig()
        return locations
    }

    override fun onLoad() { PDMBuilder(this).build().loadAllDependencies().join() }

    override fun onEnable() {
        loadConfig(this)

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            "Could not find PlaceholderAPI! This plugin is required".log()
            Bukkit.getPluginManager().disablePlugin(this)
        }

        val coolDown = conf().getProperty(Config.placeholdersUpdateCooldown).toLong()
        GetSavedLocations(this).runTaskTimerAsynchronously(this, 0, 20*coolDown)

        val papi = WorldTeleportPlaceholders(this)
        papi.register()

        val cmdManager = CommandManager(this, true)
        cmdManager.completionHandler.register("#worlds") { Bukkit.getWorlds().map(World::getName) }
        cmdManager.register(CommandWorldTeleport(this))

        "Plugin enabled!".log()
    }

    override fun onDisable() { "Plugin disabled!".log() }

}