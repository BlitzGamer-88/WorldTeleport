package me.blitzgamer_88.worldteleport

import me.blitzgamer_88.worldteleport.commands.CommandWorldTeleport
import me.blitzgamer_88.worldteleport.util.*
import me.bristermitten.pdm.PluginDependencyManager
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.CompletionResolver
import me.mattstudios.mf.base.components.MessageResolver
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Level

class WorldTeleport : JavaPlugin() {
    override fun onLoad() { PluginDependencyManager.of(this).loadAllDependencies() }
    private lateinit var commandManager: CommandManager
    private lateinit var locations: FileConfiguration
    private lateinit var locationsFile: File

    override fun onEnable() {
        this.saveDefaultConfig()
        loadConfig(this)
        loadValues(this)

        dependenciesHook("PlaceholderAPI")

        commandManager = CommandManager(this, true)
        registerMessage("cmd.no.permission") { sender -> noPermission.msg(sender) }
        registerCompletion("#worlds") { Bukkit.getWorlds().map(World::getName) }
        registerCommands(CommandWorldTeleport(this))


        "[WorldTeleport] Plugin enabled successfully!".log()
    }

    override fun onDisable() { "[WorldTeleport] Plugin disabled successfully!".log() }

    fun reload() {
        conf.reload()
        loadValues(this)
        reloadLocationsConfig()
    }

    private fun registerCommands(vararg commands: CommandBase) = commands.forEach(commandManager::register)
    private fun registerCompletion(completionId: String, resolver: CompletionResolver) = commandManager.completionHandler.register(completionId, resolver)
    private fun registerMessage(messageId: String, resolver: MessageResolver) = commandManager.messageHandler.register(messageId, resolver)


    private fun dependenciesHook(plugin: String) {
        if (Bukkit.getPluginManager().getPlugin(plugin) == null) {
            "&7Could not find: $plugin. Plugin disabled!".log()
            Bukkit.getPluginManager().disablePlugin(this)
        }
        else { "&7Successfully hooked into $plugin!".log() }
    }


    private fun reloadLocationsConfig() {
        if (!::locationsFile.isInitialized) locationsFile = File(dataFolder, "locations.yml")
        locations = YamlConfiguration.loadConfiguration(locationsFile)
    }

    fun saveLocationsConfig() {
        if (!::locations.isInitialized || !::locationsFile.isInitialized) reloadLocationsConfig()
        try { getLocationsConfig().save(locationsFile) }
        catch (ex: IOException) { logger.log(Level.SEVERE, "&7Could not save config to $locationsFile", ex) }
    }

    fun getLocationsConfig(): FileConfiguration {
        if (!::locations.isInitialized) reloadLocationsConfig()
        return locations
    }

}