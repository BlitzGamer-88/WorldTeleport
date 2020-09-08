package me.blitzgamer_88.worldteleport

import ch.jalu.configme.SettingsManager
import me.blitzgamer_88.worldteleport.cmd.CommandWorldTeleport
import me.blitzgamer_88.worldteleport.conf.WorldTeleportConfiguration
import me.blitzgamer_88.worldteleport.placeholders.WorldTeleportPlaceholders
import me.bristermitten.pdm.PDMBuilder
import me.clip.placeholderapi.PlaceholderAPIPlugin
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.io.Reader
import java.util.logging.Level


class WorldTeleport : JavaPlugin() {

    private var conf = null as? SettingsManager?

    private fun loadConfig() {
        val file = this.dataFolder.resolve("config.yml")

        if (!file.exists())
        {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        this.conf = WorldTeleportConfiguration(file)
    }

    fun conf(): SettingsManager
    {
        return checkNotNull(conf)
    }




    private var locations: FileConfiguration? = null
    private var locationsFile: File? = null

    fun reloadLocationsConfig() {
        if (locationsFile == null) {
            locationsFile = File(dataFolder, "locations.yml")
        }
        locations = YamlConfiguration.loadConfiguration(locationsFile!!)

        // Look for defaults in the jar
        val defConfigStream: Reader? = getResource("locations.yml")?.reader()
        if (defConfigStream != null){
            val defConfig = YamlConfiguration.loadConfiguration(defConfigStream)
            (locations as YamlConfiguration).setDefaults(defConfig)
        }
    }

    fun saveLocationsConfig() {
        if (locations == null || locationsFile == null) {
            return
        }
        try {
            getLocationsConfig()!!.save(locationsFile!!)
        } catch (ex: IOException) {
            logger.log(Level.SEVERE, "Could not save config to $locationsFile", ex)
        }
    }

    fun getLocationsConfig(): FileConfiguration? {
        if (locations == null) {
            reloadLocationsConfig()
        }
        return locations
    }

    override fun onEnable() {

        PDMBuilder(this).build().loadAllDependencies().join()

        loadConfig()

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {

            logger.warning("Could not find PlaceholderAPI! This plugin is required")
            Bukkit.getPluginManager().disablePlugin(this)

        }

        val papi = WorldTeleportPlaceholders(this)
        papi.register()

        val cmdManager = CommandManager(this, true)
        cmdManager.completionHandler.register("#worlds") { Bukkit.getWorlds().map(World::getName) }
        cmdManager.register(CommandWorldTeleport(this))

        logger.info("Plugin enabled!")

    }

    override fun onDisable() {

        logger.info("Plugin disabled!")

    }

}