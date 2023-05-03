package net.malteada.maltecraft.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.malteada.maltecraft.entity.ModEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;

public class ModEntitySpawn {

    public static void  addEntityspawn() {

        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.HOOR, 100, 3, 3);
        SpawnRestriction.register(ModEntities.HOOR, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);


        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.TYR, 100, 1, 2);
        SpawnRestriction.register(ModEntities.TYR, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);


        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.FORSETI, 100, 3, 3);
        SpawnRestriction.register(ModEntities.FORSETI, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);

        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.BRAGI, 100, 3, 3);
        SpawnRestriction.register(ModEntities.BRAGI, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);

        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.HELLA, 100, 3, 5);
        SpawnRestriction.register(ModEntities.HELLA, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);

        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.HEIMDALL, 100, 1, 1);
        SpawnRestriction.register(ModEntities.HEIMDALL, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);

        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER,
                ModEntities.JOTTUNHEIM, 100, 1, 1);
        SpawnRestriction.register(ModEntities.JOTTUNHEIM, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);






    }
}
