package me.blitzgamer_88.worldteleport.util

import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.conf.Config
import org.bukkit.Location

var savedLocations = HashMap<String, Location>()

var checkForBed = false

var targetNotSpecified = ""
var locationRemovedSuccessfully = ""
var couldNotTeleportTarget = ""
var locationSavedSuccessfully = ""
var teleportedSuccessfully = ""
var teleportedSuccessfullyWorldSpawn = ""
var teleportedSuccessfullyBedLocation = ""
var targetTeleportedSuccessfully = ""
var targetTeleportedSuccessfullyWorldSpawn = ""
var targetTeleportedSuccessfullyBedLocation = ""
var noWorldSpecified = ""
var noLocationSavedInThatWorld = ""
var wrongWorldName = ""
var noPermission = ""
var pluginReloaded = ""


fun registerValues(plugin: WorldTeleport) {

    val locations = plugin.getLocationsConfig() ?: return
    val keys = locations.getKeys(false)

    savedLocations.clear()
    for (key in keys) {
        val location = locations.getLocation(key) ?: continue
        savedLocations[key] = location
    }

    checkForBed = conf().getProperty(Config.checkForBed)

    targetNotSpecified = conf().getProperty(Config.targetNotSpecified)
    locationRemovedSuccessfully = conf().getProperty(Config.locationRemovedSuccessfully)
    couldNotTeleportTarget = conf().getProperty(Config.couldNotTeleportTarget)
    locationSavedSuccessfully = conf().getProperty(Config.locationSavedSuccessfully)
    teleportedSuccessfully = conf().getProperty(Config.teleportedSuccessfully)
    teleportedSuccessfullyWorldSpawn = conf().getProperty(Config.teleportedSuccessfullyWorldSpawn)
    teleportedSuccessfullyBedLocation = conf().getProperty(Config.teleportedSuccessfullyBedLocation)
    targetTeleportedSuccessfully = conf().getProperty(Config.targetTeleportedSuccessfully)
    targetTeleportedSuccessfullyWorldSpawn = conf().getProperty(Config.targetTeleportedSuccessfullyWorldSpawn)
    targetTeleportedSuccessfullyBedLocation = conf().getProperty(Config.targetTeleportedSuccessfullyBedLocation)
    noWorldSpecified = conf().getProperty(Config.noWorldSpecified)
    noLocationSavedInThatWorld = conf().getProperty(Config.noLocationSavedInThatWorld)
    wrongWorldName = conf().getProperty(Config.wrongWorldName)
    noPermission = conf().getProperty(Config.noPermission)
    pluginReloaded = conf().getProperty(Config.pluginReloaded)
}