package net.malteada.maltecraft.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEntities {

    public static final EntityType<HoorEntity> HOOR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "hoor"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HoorEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0F, 2.1F)).build());

    public static final EntityType<TyrEntity> TYR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "tyr"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, TyrEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0F, 3.5F)).build());
    public static final EntityType<BragiEntity> BRAGI = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "bragi"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BragiEntity::new)
                    .dimensions(EntityDimensions.fixed(1.2F, 2.2F)).build());

    public static final EntityType<ForsetiEntity> FORSETI = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "forseti"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ForsetiEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 2.1F)).build());

    public static final EntityType<HellaEntity> HELLA = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "hella"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HellaEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 2.1F)).build());

    public static final EntityType<HeimdallEntity> HEIMDALL = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "heimdall"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HeimdallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 2.4F)).build());
    public static final EntityType<JottunheimEntity> JOTTUNHEIM = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Maltecraft.MOD_ID, "jottunheim"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, JottunheimEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4F, 3.8F)).build());

}
