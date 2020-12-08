package me.blitzgamer_88.worldteleport.conf

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer

internal object Config : SettingsHolder {

    @JvmField
    @Comment("If this is enabled the plugin will check if the player has his spawnpoint", "set in that world and teleport him there instead of the world spawnpoint or the saved location.")
    val checkForBed: Property<Boolean> = PropertyInitializer.newProperty("checkForBed", false)

    @JvmField
    @Comment("Customize the messages that are sent by the plugin:")
    val targetNotSpecified: Property<String> = PropertyInitializer.newProperty("targetNotSpecified", "&cYou need to specify a target!")
    @JvmField
    val locationRemovedSuccessfully: Property<String> = PropertyInitializer.newProperty("locationRemovedSuccessfully", "&aThe teleport location has been removed successfully.")
    @JvmField
    val couldNotTeleportTarget: Property<String> = PropertyInitializer.newProperty("couldNotTeleportTarget", "&cCould not teleport %player_name%.")
    @JvmField
    val locationSavedSuccessfully: Property<String> = PropertyInitializer.newProperty("locationSavedSuccessfully", "&aThe new teleport location has been set successfully.")
    @JvmField
    val teleportedSuccessfully: Property<String> = PropertyInitializer.newProperty("teleportedSuccessfully", "&aYou have been teleported successfully.")
    @JvmField
    val teleportedSuccessfullyWorldSpawn: Property<String> = PropertyInitializer.newProperty("teleportedSuccessfullyWorldSpawn", "&aYou have been teleported at that world''s spawnpoint.")
    @JvmField
    val teleportedSuccessfullyBedLocation: Property<String> = PropertyInitializer.newProperty("teleportedSuccessfullyBedLocation", "&aYou have been teleported at your bed spawnpoint.")
    @JvmField
    val targetTeleportedSuccessfully: Property<String> = PropertyInitializer.newProperty("targetTeleportedSuccessfully", "&a%player_name% has been teleported successfully.")
    @JvmField
    val targetTeleportedSuccessfullyWorldSpawn: Property<String> = PropertyInitializer.newProperty("targetTeleportedSuccessfullyWorldSpawn", "&a%player_name% has been teleported at that world''s spawnpoint.")
    @JvmField
    val targetTeleportedSuccessfullyBedLocation: Property<String> = PropertyInitializer.newProperty("targetTeleportedSuccessfullyBedLocation", "&a%player_name% has been teleported at this bed spawnpoint.")
    @JvmField
    val noWorldSpecified: Property<String> = PropertyInitializer.newProperty("noWorldSpecified", "&cYou need to specify a world name.")
    @JvmField
    val noLocationSavedInThatWorld: Property<String> = PropertyInitializer.newProperty("noLocationSavedInThatWorld", "&cThere is no location saved in this world.")
    @JvmField
    val wrongWorldName: Property<String> = PropertyInitializer.newProperty("wrongWorldName", "&cWrong world name!")
    @JvmField
    val noPermission: Property<String> = PropertyInitializer.newProperty("noPermission", "&cYou do not have permission to do that.")
    @JvmField
    val pluginReloaded: Property<String> = PropertyInitializer.newProperty("pluginReloaded", "&cPlugin reloaded successfully!")

}