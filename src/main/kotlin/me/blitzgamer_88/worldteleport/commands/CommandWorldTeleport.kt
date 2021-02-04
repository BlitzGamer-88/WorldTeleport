package me.blitzgamer_88.worldteleport.commands

import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.util.*
import me.clip.placeholderapi.PlaceholderAPI
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command("worldtp")
@Alias("wtp")
class CommandWorldTeleport(private val plugin: WorldTeleport) : CommandBase() {

    @Default
    fun worldTeleport(sender: CommandSender, @Completion("#worlds") world: World, @Optional @Completion("#players") target: Player?){

        if (target == null && sender !is Player) {
            targetNotSpecified.msg(sender)
            return
        }

        val player = target ?: sender as Player

        if (target != null && !sender.hasPermission("worldteleport.teleport.others")){
            noPermission.msg(sender)
            return
        }

        if (target == null && !player.hasPermission("worldteleport.teleport") && !player.hasPermission("worldteleport.teleport.${world.name}")) {
            noPermission.msg(player)
            return
        }

        if (player == target && player.hasPermission("worldteleport.bypass")){
            sender.sendMessage(PlaceholderAPI.setPlaceholders(target, couldNotTeleportTarget))
            return
        }

        val location = if (checkForBed && player.bedSpawnLocation != null && player.bedSpawnLocation!!.world == world) player.bedSpawnLocation
                else if (savedLocations[world.name] != null) savedLocations[world.name]
                else world.spawnLocation
        if (location == null) {
            "&c&lSomething went wrong!".msg(sender)
            return
        }

        val bedLocation = player.bedSpawnLocation
        val savedLocation = savedLocations[world.name]

        location.world.getChunkAtAsync(location).thenAccept {
            player.teleportAsync(location).thenAccept {
                when {
                    checkForBed && location == bedLocation -> teleportedSuccessfullyBedLocation.msg(player)
                    location == savedLocation -> teleportedSuccessfully.msg(player)
                    else -> teleportedSuccessfullyWorldSpawn.msg(sender)
                }
            }
        }
        if (player == sender) return
        when {
            checkForBed && location == bedLocation -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyBedLocation))
            location == savedLocation -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfully))
            else -> sender.sendMessage(PlaceholderAPI.setPlaceholders(target, targetTeleportedSuccessfullyWorldSpawn))
        }
    }


    @SubCommand("set")
    fun worldSetTeleport(sender: Player){
        if (!sender.hasPermission("worldteleport.setlocation") && !sender.hasPermission("worldteleport.setlocation.${sender.world.name}")){
            noPermission.msg(sender)
            return
        }

        plugin.getLocationsConfig().set(sender.world.name, sender.location)
        plugin.saveLocationsConfig()
        plugin.reload()
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

        val location = plugin.getLocationsConfig().getLocation(permWorld)
        if (location == null) {
            noLocationSavedInThatWorld.msg(sender)
            return
        }
        plugin.getLocationsConfig().set(permWorld, null)
        plugin.saveLocationsConfig()
        plugin.reload()
        locationRemovedSuccessfully.msg(sender)
    }

    @SubCommand("reload")
    @Permission("worldteleport.reload")
    fun reloadLocations(sender: CommandSender){
        plugin.reload()
        pluginReloaded.msg(sender)
    }
}