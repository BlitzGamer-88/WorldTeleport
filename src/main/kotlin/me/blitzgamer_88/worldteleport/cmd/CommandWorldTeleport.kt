package me.blitzgamer_88.worldteleport.cmd

import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.conf.Config
import me.blitzgamer_88.worldteleport.conf.WorldTeleportConfiguration
import me.blitzgamer_88.worldteleport.util.color
import me.clip.placeholderapi.PlaceholderAPI
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


// TODO: SAVE LOCATIONS TO A DATABASE (IF POSSIBLE SQL/SQLITE - IF NOT THEN YAML)



@Command("worldtp")
@Alias("wtp")
class CommandWorldTeleport(private val mainClass: WorldTeleport) : CommandBase() {

    private val worldTeleportPermission = mainClass.conf().getProperty(Config.worldTeleportPermission).color()
    private val worldSetTeleportPermission = mainClass.conf().getProperty(Config.worldSetTeleportPermission).color()
    private val bypassTeleportPermission = mainClass.conf().getProperty(Config.bypassTeleportPermission).color()
    private val locationsReloadPermission = mainClass.conf().getProperty(Config.locationsReloadPermission).color()

    private val couldNotTeleportTarget = mainClass.conf().getProperty(Config.couldNotTeleportTarget).color()
    private val noPermission = mainClass.conf().getProperty(Config.noPermission).color()
    private val wrongWorldName = mainClass.conf().getProperty(Config.wrongWorldName).color()

    private val locationSavedSuccessfully = mainClass.conf().getProperty(Config.locationSavedSuccessfully).color()
    private val locationRemovedSuccessfully = mainClass.conf().getProperty(Config.locationRemovedSuccessfully).color()
    private val teleportedSuccessfully =  mainClass.conf().getProperty(Config.teleportedSuccessfully).color()
    private val teleportedSuccessfullyWorldSpawn = mainClass.conf().getProperty(Config.teleportedSuccessfullyWorldSpawn).color()
    private val targetTeleportedSuccessfully = mainClass.conf().getProperty(Config.targetTeleportedSuccessfully).color()
    private val targetTeleportedSuccessfullyWorldSpawn = mainClass.conf().getProperty(Config.targetTeleportedSuccessfullyWorldSpawn).color()

    private val pluginReloaded = mainClass.conf().getProperty(Config.pluginReloaded).color()

    @Default
    fun defaultCommand(sender: CommandSender){
        sender.sendMessage("&eWorldTeleport &8by &6BlitzGamer_88".color())
    }

    @SubCommand("tp")
    @Alias("teleport")
    fun worldTeleport(sender: CommandSender, @Completion("#worlds") worldName: String, @Optional @Completion("#players") target: Player?){

        if (sender is Player){

            val perWorldTeleportPermission = "worldteleport.teleport.$worldName"

            // Checks for command usage permission.

            if (!sender.hasPermission(worldTeleportPermission) || !sender.hasPermission(perWorldTeleportPermission)){
                sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
                return
            }

            // Checks if world exists.

            val world = Bukkit.getServer().getWorld(worldName)
            if (world == null) {
                sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, wrongWorldName))
                return
            }

            // Checks for target.

            if (target == null) {
                val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
                if (loc == null){
                    sender.teleport(world.spawnLocation)
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, teleportedSuccessfullyWorldSpawn))
                    return
                }
                sender.teleport(loc)
                sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, teleportedSuccessfully))
                return
            }

            // Checks for bypass permissions.

            if(target.hasPermission(bypassTeleportPermission)){
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
                return
            }

            // Checks if there is a teleport location for the world.

            val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
            if (loc == null){
                target.teleport(world.spawnLocation)
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
                return
            }
            target.teleport(loc)
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
            return
        }

        // IF SENDER IS NOT PLAYER

        // Checks if world exists.

        val world = Bukkit.getServer().getWorld(worldName)
        if (world == null) {
            sender.sendMessage(wrongWorldName)
            return
        }

        // Checks for target.

        if (target == null) {
            sender.sendMessage("&cUse: &f/worldtp teleport world Player".color())
            return
        }

        // Checks for bypass permissions.

        if(target.hasPermission(bypassTeleportPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
            return
        }

        // Checks if there is a teleport location for the world.

        val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
        if (loc == null) {
            target.teleport(world.spawnLocation)
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
            return
        }
        target.teleport(loc)
        sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
        return
    }

    @SubCommand("settp")
    @Alias("set")
    fun worldSetTeleport(sender: Player){
        if (!sender.hasPermission(worldSetTeleportPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
            return
        }
        mainClass.getLocationsConfig()?.set(sender.world.name, sender.location)
        mainClass.saveLocationsConfig()
        sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, locationSavedSuccessfully))
    }

    @SubCommand("removetp")
    @Alias("remove")
    fun worldRemoveTeleport(sender: CommandSender, @Completion("#worlds") worldName: String){
        if (mainClass.getLocationsConfig()?.getLocation(worldName) != null){
            mainClass.getLocationsConfig()?.set(worldName, null)
            mainClass.saveLocationsConfig()
            sender.sendMessage(locationRemovedSuccessfully)
        }
    }

    @SubCommand("reload")
    fun reloadLocations(sender: CommandSender){
        if (sender is Player && !sender.hasPermission(locationsReloadPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
            return
        }
        mainClass.reloadLocationsConfig()
        sender.sendMessage(pluginReloaded)
    }
}