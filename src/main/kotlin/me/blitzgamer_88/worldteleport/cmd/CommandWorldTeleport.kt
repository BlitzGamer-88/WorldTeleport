package me.blitzgamer_88.worldteleport.cmd

import io.papermc.lib.PaperLib
import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.util.*
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
    fun worldTeleport(sender: CommandSender, @Completion("#worlds") worldName: String, @Optional @Completion("#players") target: Player?){

        if (target == null && sender !is Player) {
            targetNotSpecified.msg(sender)
            return
        }

        val player = target ?: sender as Player

        if (target != null && sender is Player && !sender.hasPermission("worldteleport.teleport.others")){
            noPermission.msg(sender)
            return
        }

        if (target == null && !player.hasPermission("worldteleport.teleport") && !player.hasPermission("worldteleport.teleport.$worldName")) {
            noPermission.msg(player)
            return
        }

        if (player == target && player.hasPermission("worldteleport.bypass")){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
            return
        }

        val world = Bukkit.getServer().getWorld(worldName)
        if (world == null) {
            wrongWorldName.msg(sender)
            return
        }

        val loc = savedLocations[worldName]
        val bedLocation = player.bedSpawnLocation

        var teleportLocation = if (checkForBed && bedLocation != null && bedLocation.world != null && bedLocation.world == world) bedLocation else loc
        if (teleportLocation == null) teleportLocation = world.spawnLocation

        PaperLib.teleportAsync(player, teleportLocation)
        if (player != sender) {
            when (teleportLocation) {
                bedLocation -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyBedLocation))
                loc -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
                else -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
            }
        }
        when (teleportLocation) {
            bedLocation -> teleportedSuccessfullyBedLocation.msg(player)
            loc -> teleportedSuccessfully.msg(player)
            else -> teleportedSuccessfullyWorldSpawn.msg(sender)
        }
    }


    @SubCommand("set")
    fun worldSetTeleport(sender: Player){
        if (!sender.hasPermission("worldteleport.setlocation") && !sender.hasPermission("worldteleport.setlocation.${sender.world.name}")){
            noPermission.msg(sender)
            return
        }

        mainClass.getLocationsConfig()?.set(sender.world.name, sender.location)
        mainClass.saveLocationsConfig()
        mainClass.reload()
        locationSavedSuccessfully.msg(sender)
    }

    @SubCommand("remove")
    fun worldRemoveTeleport(sender: CommandSender, @Optional @Completion("#worlds") worldName: String?){

        val permWorld = if (worldName == null && sender is Player) sender.world.name else worldName

        if (permWorld == null) {
            noWorldSpecified.msg(sender)
            return
        }

        if (sender is Player && !sender.hasPermission("worldteleport.removelocation") && !sender.hasPermission("worldteleport.removelocation.$permWorld")) {
            noPermission.msg(sender)
            return
        }

        val loc = mainClass.getLocationsConfig()?.getLocation(permWorld)
        if (loc == null) {
            noLocationSavedInThatWorld.msg(sender)
            return
        }
        mainClass.getLocationsConfig()?.set(permWorld, null)
        mainClass.saveLocationsConfig()
        mainClass.reload()
        locationRemovedSuccessfully.msg(sender)
    }

    @SubCommand("reload")
    fun reloadLocations(sender: CommandSender){
        if (sender is Player && !sender.hasPermission("worldteleport.reload")){
            noPermission.msg(sender)
            return
        }
        mainClass.reload()
        pluginReloaded.msg(sender)
    }
}