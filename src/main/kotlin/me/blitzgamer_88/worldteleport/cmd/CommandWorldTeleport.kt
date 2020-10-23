package me.blitzgamer_88.worldteleport.cmd

import io.papermc.lib.PaperLib
import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.conf.Config
import me.blitzgamer_88.worldteleport.util.color
import me.blitzgamer_88.worldteleport.util.conf
import me.blitzgamer_88.worldteleport.util.msg
import me.clip.placeholderapi.PlaceholderAPI
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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

        val worldTeleportPermission = conf().getProperty(Config.worldTeleportPermission)
        val bypassTeleportPermission = conf().getProperty(Config.bypassTeleportPermission)

        val checkForBed = conf().getProperty(Config.checkForBed)

        val couldNotTeleportTarget = conf().getProperty(Config.couldNotTeleportTarget).color()
        val noPermission = conf().getProperty(Config.noPermission).color()
        val wrongWorldName = conf().getProperty(Config.wrongWorldName).color()

        val teleportedSuccessfully =  conf().getProperty(Config.teleportedSuccessfully).color()
        val teleportedSuccessfullyWorldSpawn = conf().getProperty(Config.teleportedSuccessfullyWorldSpawn).color()
        val targetTeleportedSuccessfully = conf().getProperty(Config.targetTeleportedSuccessfully).color()
        val targetTeleportedSuccessfullyWorldSpawn = conf().getProperty(Config.targetTeleportedSuccessfullyWorldSpawn).color()
        val teleportedSuccessfullyBedLocation = conf().getProperty(Config.teleportedSuccessfullyBedLocation).color()
        val targetTeleportedSuccessfullyBedLocation = conf().getProperty(Config.targetTeleportedSuccessfullyBedLocation).color()

        if (sender is Player) {

            val perWorldTeleportPermission = "worldteleport.teleport.$worldName"

            // Checks for command usage permission.

            if (!sender.hasPermission(worldTeleportPermission) && !sender.hasPermission(perWorldTeleportPermission)){
                noPermission.msg(sender)
                return
            }

            // Checks if world exists.

            val world = Bukkit.getServer().getWorld(worldName)
            if (world == null) {
                wrongWorldName.msg(sender)
                return
            }

            // Checks for target.

            if (target == null) {
                val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
                val bedLocation = sender.bedSpawnLocation
                if (checkForBed && bedLocation != null) {
                    val bedWorld = bedLocation.world
                    if (bedWorld != null && bedWorld == world) {
                        PaperLib.teleportAsync(sender, bedLocation)
                        teleportedSuccessfullyBedLocation.msg(sender)
                        return
                    }
                }
                if (loc == null) {
                    PaperLib.teleportAsync(sender, world.spawnLocation)
                    teleportedSuccessfullyWorldSpawn.msg(sender)
                    return
                }
                PaperLib.teleportAsync(sender, loc)
                teleportedSuccessfully.msg(sender)
                return
            }

            // Checks for bypass permissions.

            if(target.hasPermission(bypassTeleportPermission)){
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
                return
            }

            // Checks if there is a teleport location for the world.

            val bedLocation = target.bedSpawnLocation
            if (checkForBed && bedLocation != null) {
                val bedWorld = bedLocation.world
                if (bedWorld != null && bedWorld == world) {
                    PaperLib.teleportAsync(target, world.spawnLocation)
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyBedLocation))
                    return
                }
            }
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

        // Checks for target.

        if (target == null) {
            "&cUse: &f/worldtp teleport <world> <player>".msg(sender)
            return
        }

        // Checks if world exists.

        val world = Bukkit.getServer().getWorld(worldName)
        if (world == null) {
            sender.sendMessage(wrongWorldName)
            return
        }

        // Checks for bypass permissions.

        if(target.hasPermission(bypassTeleportPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
            return
        }

        // Checks if there is a teleport location for the world.

        val bedLocation = target.bedSpawnLocation
        if (checkForBed && bedLocation != null) {
            val bedWorld = bedLocation.world
            if (bedWorld != null && bedWorld == world) {
                PaperLib.teleportAsync(target, world.spawnLocation)
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyBedLocation))
                return
            }
        }

        val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
        if (loc == null) {
            PaperLib.teleportAsync(target, world.spawnLocation)
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
            return
        }
        PaperLib.teleportAsync(target, loc)
        sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
    }


    @SubCommand("settp")
    @Alias("set")
    fun worldSetTeleport(sender: Player){

        val worldSetTeleportPermission = conf().getProperty(Config.worldSetTeleportPermission)
        val noPermission = conf().getProperty(Config.noPermission).color()
        val locationSavedSuccessfully = conf().getProperty(Config.locationSavedSuccessfully).color()

        val perWorldSetPermission = "worldteleport.setlocation.${sender.world.name}"

        if (!sender.hasPermission(worldSetTeleportPermission) && !sender.hasPermission(perWorldSetPermission)){
            noPermission.msg(sender)
            return
        }

        mainClass.getLocationsConfig()?.set(sender.world.name, sender.location)
        mainClass.saveLocationsConfig()
        locationSavedSuccessfully.msg(sender)
    }

    @SubCommand("removetp")
    @Alias("remove")
    fun worldRemoveTeleport(sender: CommandSender, @Optional @Completion("#worlds") worldName: String?){

        val worldRemoveTeleportPermission = conf().getProperty(Config.worldRemoveTeleportPermission)
        val locationRemovedSuccessfully = conf().getProperty(Config.locationRemovedSuccessfully).color()
        val noPermission = conf().getProperty(Config.noPermission).color()
        val noWorldSpecified = conf().getProperty(Config.noWorldSpecified).color()
        val noLocationSavedInThatWorld = conf().getProperty(Config.noLocationSavedInThatWorld).color()

        if (sender is Player) {

            val senderWorldName = sender.world.name
            val permWorld = worldName ?: senderWorldName

            val perWorldRemovePermission = "worldteleport.removelocation.$permWorld"

            if (!sender.hasPermission(worldRemoveTeleportPermission) && !sender.hasPermission(perWorldRemovePermission)) {
                noPermission.msg(sender)
                return
            }
            if (mainClass.getLocationsConfig()?.getLocation(permWorld) != null) {
                mainClass.getLocationsConfig()?.set(permWorld, null)
                mainClass.saveLocationsConfig()
                locationRemovedSuccessfully.msg(sender)
                return
            }
            noLocationSavedInThatWorld.msg(sender)
            return
        }

        if (worldName == null){
            noWorldSpecified.msg(sender)
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

        val reloadPermission = conf().getProperty(Config.reloadPermission)
        val noPermission = conf().getProperty(Config.noPermission).color()
        val pluginReloaded = conf().getProperty(Config.pluginReloaded).color()

        if (sender is Player && !sender.hasPermission(reloadPermission)){
            noPermission.msg(sender)
            return
        }
        mainClass.reloadLocationsConfig()
        conf().reload()
        sender.sendMessage(pluginReloaded)
    }
}