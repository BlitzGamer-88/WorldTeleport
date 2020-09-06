package me.blitzgamer_88.worldteleport.cmd

import io.papermc.lib.PaperLib
import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.conf.Config
import me.blitzgamer_88.worldteleport.util.color
import me.clip.placeholderapi.PlaceholderAPI
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


// TODO: SAVE LOCATIONS TO A DATABASE (IF POSSIBLE SQL/SQLITE - IF NOT THEN YAML)



@Command("worldtp")
@Alias("wtp")
class CommandWorldTeleport(private val mainClass: WorldTeleport) : CommandBase() {

    @Default
    fun defaultCommand(sender: CommandSender){
        sender.sendMessage("&eWorldTeleport &8by &6BlitzGamer_88".color())
    }

    @SubCommand("tp")
    @Alias("teleport")
    fun worldTeleport(sender: CommandSender, @Completion("#worlds") worldName: String, @Optional @Completion("#players") target: Player?){

        val worldTeleportPermission = mainClass.conf().getProperty(Config.worldTeleportPermission)
        val bypassTeleportPermission = mainClass.conf().getProperty(Config.bypassTeleportPermission)

        val couldNotTeleportTarget = mainClass.conf().getProperty(Config.couldNotTeleportTarget).color()
        val noPermission = mainClass.conf().getProperty(Config.noPermission).color()
        val wrongWorldName = mainClass.conf().getProperty(Config.wrongWorldName).color()

        val teleportedSuccessfully =  mainClass.conf().getProperty(Config.teleportedSuccessfully).color()
        val teleportedSuccessfullyWorldSpawn = mainClass.conf().getProperty(Config.teleportedSuccessfullyWorldSpawn).color()
        val targetTeleportedSuccessfully = mainClass.conf().getProperty(Config.targetTeleportedSuccessfully).color()
        val targetTeleportedSuccessfullyWorldSpawn = mainClass.conf().getProperty(Config.targetTeleportedSuccessfullyWorldSpawn).color()

        if (sender is Player) {

            val perWorldTeleportPermission = "worldteleport.teleport.$worldName"

            // Checks for command usage permission.

            if (!sender.hasPermission(worldTeleportPermission) && !sender.hasPermission(perWorldTeleportPermission)){
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
                if (loc == null) {
                    PaperLib.teleportAsync(sender, world.spawnLocation)
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, teleportedSuccessfullyWorldSpawn))
                    return
                }
                PaperLib.teleportAsync(sender, loc)
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
                PaperLib.teleportAsync(target, world.spawnLocation)
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
                return
            }
            PaperLib.teleportAsync(target, loc)
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
            PaperLib.teleportAsync(target, world.spawnLocation)
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
            return
        }
        PaperLib.teleportAsync(target, loc)
        sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
        return
    }


    @SubCommand("settp")
    @Alias("set")
    fun worldSetTeleport(sender: Player){

        val worldSetTeleportPermission = mainClass.conf().getProperty(Config.worldSetTeleportPermission)
        val noPermission = mainClass.conf().getProperty(Config.noPermission).color()
        val locationSavedSuccessfully = mainClass.conf().getProperty(Config.locationSavedSuccessfully).color()

        val perWorldSetPermission = "worldteleport.setlocation.${sender.world.name}"

        if (sender.hasPermission(worldSetTeleportPermission) || sender.hasPermission(perWorldSetPermission)){
            mainClass.getLocationsConfig()?.set(sender.world.name, sender.location)
            mainClass.saveLocationsConfig()
            sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, locationSavedSuccessfully))
            return
        }

        sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
        return
    }

    @SubCommand("removetp")
    @Alias("remove")
    fun worldRemoveTeleport(sender: CommandSender, @Optional @Completion("#worlds") worldName: String?){

        val worldRemoveTeleportPermission = mainClass.conf().getProperty(Config.worldRemoveTeleportPermission)
        val locationRemovedSuccessfully = mainClass.conf().getProperty(Config.locationRemovedSuccessfully).color()
        val noPermission = mainClass.conf().getProperty(Config.noPermission).color()
        val noWorldSpecified = mainClass.conf().getProperty(Config.noWorldSpecified).color()
        val noLocationSavedInThatWorld = mainClass.conf().getProperty(Config.noLocationSavedInThatWorld).color()

        if (sender is Player) {

            if (worldName == null) {

                val senderWorldName = sender.world.name
                val perWorldRemovePermission = "worldteleport.removelocation.$senderWorldName"

                if (!sender.hasPermission(worldRemoveTeleportPermission) && !sender.hasPermission(perWorldRemovePermission)) {
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
                    return
                }

                if (mainClass.getLocationsConfig()?.getLocation(senderWorldName) != null) {
                    mainClass.getLocationsConfig()?.set(senderWorldName, null)
                    mainClass.saveLocationsConfig()
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, locationRemovedSuccessfully))
                    return
                } else {
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noLocationSavedInThatWorld))
                    return
                }

            } else {

                val perWorldRemovePermission = "worldteleport.removelocation.$worldName"

                if (!sender.hasPermission(worldRemoveTeleportPermission) && !sender.hasPermission(perWorldRemovePermission)) {
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
                    return
                }

                if (mainClass.getLocationsConfig()?.getLocation(worldName) != null) {
                    mainClass.getLocationsConfig()?.set(worldName, null)
                    mainClass.saveLocationsConfig()
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, locationRemovedSuccessfully))
                    return
                } else {
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noLocationSavedInThatWorld))
                    return
                }
            }
        }

        if (worldName == null){
            sender.sendMessage(noWorldSpecified)
            return
        }

        if (mainClass.getLocationsConfig()?.getLocation(worldName) != null) {
            mainClass.getLocationsConfig()?.set(worldName, null)
            mainClass.saveLocationsConfig()
            sender.sendMessage(locationRemovedSuccessfully)
            return
        } else {
            sender.sendMessage(noLocationSavedInThatWorld)
            return
        }
    }

    @SubCommand("reload")
    fun reloadLocations(sender: CommandSender){

        val reloadPermission = mainClass.conf().getProperty(Config.reloadPermission)
        val noPermission = mainClass.conf().getProperty(Config.noPermission).color()
        val pluginReloaded = mainClass.conf().getProperty(Config.pluginReloaded).color()

        if (sender is Player && !sender.hasPermission(reloadPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(sender, noPermission))
            return
        }
        mainClass.reloadLocationsConfig()
        mainClass.conf().reload()
        sender.sendMessage(pluginReloaded)
    }
}