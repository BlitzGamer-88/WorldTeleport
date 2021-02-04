package me.blitzgamer_88.worldteleport.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer

internal object Config : SettingsHolder {

    @JvmField
    @Comment("If this is enabled the plugin will check if the player has his spawnpoint", "set in that world and teleport him there instead of the world spawnpoint or the saved location.")
    val checkForBed: Property<Boolean> = PropertyInitializer.newProperty("check-for-bed", false)

    @JvmField
    @Comment("Customize the messages that are sent by the plugin:")
    val targetNotSpecified: Property<String> = PropertyInitializer.newProperty("NO-TARGET-SPECIFIED", "&cYou need to specify a target!")
    @JvmField
    val locationRemovedSuccessfully: Property<String> = PropertyInitializer.newProperty("LOCATION-REMOVED-SUCCESS", "&aThe teleport location has been removed successfully.")
    @JvmField
    val couldNotTeleportTarget: Property<String> = PropertyInitializer.newProperty("TELEPORT-FAIL-TARGET", "&cCould not teleport %player_name%.")
    @JvmField
    val locationSavedSuccessfully: Property<String> = PropertyInitializer.newProperty("LOCATION-SAVED-SUCCESS", "&aThe new teleport location has been set successfully.")
    @JvmField
    val teleportedSuccessfully: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS", "&aYou have been teleported successfully.")
    @JvmField
    val teleportedSuccessfullyWorldSpawn: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS-WORLD-SPAWN", "&aYou have been teleported at that world''s spawnpoint.")
    @JvmField
    val teleportedSuccessfullyBedLocation: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS-BED-SPAWN", "&aYou have been teleported at your bed spawnpoint.")
    @JvmField
    val targetTeleportedSuccessfully: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS-TARGET", "&a%player_name% has been teleported successfully.")
    @JvmField
    val targetTeleportedSuccessfullyWorldSpawn: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS-TARGET-WORLD-SPAWN", "&a%player_name% has been teleported at that world''s spawnpoint.")
    @JvmField
    val targetTeleportedSuccessfullyBedLocation: Property<String> = PropertyInitializer.newProperty("TELEPORT-SUCCESS-TARGET-BED-LOCATION", "&a%player_name% has been teleported at this bed spawnpoint.")
    @JvmField
    val noWorldSpecified: Property<String> = PropertyInitializer.newProperty("WORLD-NOT-SPECIFIED", "&cYou need to specify a world name.")
    @JvmField
    val noLocationSavedInThatWorld: Property<String> = PropertyInitializer.newProperty("LOCATION-NOT-FOUND", "&cThere is no location saved in this world.")
    @JvmField
    val noPermission: Property<String> = PropertyInitializer.newProperty("NO-PERMISSION", "&cYou do not have permission to do that.")
    @JvmField
    val pluginReloaded: Property<String> = PropertyInitializer.newProperty("PLUGIN-RELOADED", "&cPlugin reloaded successfully!")

}