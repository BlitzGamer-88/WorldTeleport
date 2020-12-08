package me.blitzgamer_88.worldteleport

import me.blitzgamer_88.worldteleport.cmd.CommandWorldTeleport
import me.blitzgamer_88.worldteleport.util.*
import me.bristermitten.pdm.PDMBuilder
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Level


class WorldTeleport : JavaPlugin() {

    private var locations: FileConfiguration? = null
    private var locationsFile: File? = null

    override fun onLoad() { PDMBuilder(this).build().loadAllDependencies().join() }

    override fun onEnable() {

        this.saveDefaultConfig()
        loadConfig(this)

        dependenciesHook()

        registerValues(this)
        registerCommands()

        "&7[WorldTeleport] Plugin enabled successfully!".log()
    }

    override fun onDisable() { "&7[WorldTeleport] Plugin disabled successfully!".log() }

    fun reload() {
        conf().reload()
        registerValues(this)
        reloadLocationsConfig()
    }

    private fun dependenciesHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            "&7Could not find: PlaceholderAPI. Plugin disabled!".log()
            Bukkit.getPluginManager().disablePlugin(this)
        }
        else { "&7Successfully hooked into PlaceholderAPI!".log() }
    }

    private fun registerCommands() {
        val cmdManager = CommandManager(this, true)
        cmdManager.completionHandler.register("#worlds") { Bukkit.getWorlds().map(World::getName) }
        cmdManager.register(CommandWorldTeleport(this))
    }


    private fun reloadLocationsConfig() {
        if (locationsFile == null) locationsFile = File(dataFolder, "locations.yml")
        locations = YamlConfiguration.loadConfiguration(locationsFile!!)
    }

    fun saveLocationsConfig() {
        if (locations == null || locationsFile == null) return
        try { getLocationsConfig()!!.save(locationsFile!!) }
        catch (ex: IOException) { logger.log(Level.SEVERE, "&7Could not save config to $locationsFile", ex) }
    }

    fun getLocationsConfig(): FileConfiguration? {
        if (locations == null) reloadLocationsConfig()
        return locations
    }

}