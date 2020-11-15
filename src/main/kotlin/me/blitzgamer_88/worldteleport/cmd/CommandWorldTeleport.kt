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

        val player = target ?: sender

        if (player !is Player) {
            "&cUse: &f/worldtp teleport <world> <player>".msg(sender)
            return
        }

        val perWorldTeleportPermission = "worldteleport.teleport.$worldName"

        if (sender != player && sender is Player && !sender.hasPermission(worldTeleportPermission) && !sender.hasPermission(perWorldTeleportPermission)){
            noPermission.msg(sender)
            return
        }

        if(player == target && player.hasPermission(bypassTeleportPermission)){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
            return
        }

        val world = Bukkit.getServer().getWorld(worldName)
        if (world == null) {
            sender.sendMessage(wrongWorldName)
            return
        }

        val loc = mainClass.getLocationsConfig()?.getLocation(worldName)
        val bedLocation = player.bedSpawnLocation

        if (checkForBed && bedLocation != null) {
            val bedWorld = bedLocation.world
            if (bedWorld != null && bedWorld == world) {
                PaperLib.teleportAsync(player, bedLocation)
                if (player == sender) teleportedSuccessfullyBedLocation.msg(sender)
                else sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyBedLocation))
                return
            }
        }

        if (loc == null) {
            PaperLib.teleportAsync(player, world.spawnLocation)
            if (player == sender) teleportedSuccessfullyWorldSpawn.msg(sender)
            else sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
            return
        }
        PaperLib.teleportAsync(player, loc)
        if (player == sender) teleportedSuccessfully.msg(sender)
        else sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
        return
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

        val permWorld = if (worldName == null && sender is Player) sender.world.name else worldName

        if (permWorld == null && sender !is Player) {
            noWorldSpecified.msg(sender)
            return
        }

        val perWorldRemovePermission = "worldteleport.removelocation.$permWorld"
        if (sender is Player && !sender.hasPermission(worldRemoveTeleportPermission) && !sender.hasPermission(perWorldRemovePermission)) {
            noPermission.msg(sender)
            return
        }

        val loc = permWorld?.let { mainClass.getLocationsConfig()?.getLocation(it) }
        if (loc == null) {
            noLocationSavedInThatWorld.msg(sender)
            return
        }
        mainClass.getLocationsConfig()?.set(permWorld, null)
        mainClass.saveLocationsConfig()
        locationRemovedSuccessfully.msg(sender)
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
        pluginReloaded.msg(sender)
    }
}