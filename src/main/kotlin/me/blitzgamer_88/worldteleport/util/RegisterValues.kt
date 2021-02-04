package me.blitzgamer_88.worldteleport.util

import me.blitzgamer_88.worldteleport.WorldTeleport
import me.blitzgamer_88.worldteleport.config.Config
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
var noPermission = ""
var pluginReloaded = ""


fun loadValues(plugin: WorldTeleport) {

    savedLocations.clear()
    val locations = plugin.getLocationsConfig()
    locations.getKeys(false).forEach {
        if (locations.getLocation(it) != null) savedLocations[it] = locations.getLocation(it)!!
    }

    checkForBed = conf.getProperty(Config.checkForBed)

    targetNotSpecified = conf.getProperty(Config.targetNotSpecified)
    locationRemovedSuccessfully = conf.getProperty(Config.locationRemovedSuccessfully)
    couldNotTeleportTarget = conf.getProperty(Config.couldNotTeleportTarget)
    locationSavedSuccessfully = conf.getProperty(Config.locationSavedSuccessfully)
    teleportedSuccessfully = conf.getProperty(Config.teleportedSuccessfully)
    teleportedSuccessfullyWorldSpawn = conf.getProperty(Config.teleportedSuccessfullyWorldSpawn)
    teleportedSuccessfullyBedLocation = conf.getProperty(Config.teleportedSuccessfullyBedLocation)
    targetTeleportedSuccessfully = conf.getProperty(Config.targetTeleportedSuccessfully)
    targetTeleportedSuccessfullyWorldSpawn = conf.getProperty(Config.targetTeleportedSuccessfullyWorldSpawn)
    targetTeleportedSuccessfullyBedLocation = conf.getProperty(Config.targetTeleportedSuccessfullyBedLocation)
    noWorldSpecified = conf.getProperty(Config.noWorldSpecified)
    noLocationSavedInThatWorld = conf.getProperty(Config.noLocationSavedInThatWorld)
    noPermission = conf.getProperty(Config.noPermission)
    pluginReloaded = conf.getProperty(Config.pluginReloaded)
}