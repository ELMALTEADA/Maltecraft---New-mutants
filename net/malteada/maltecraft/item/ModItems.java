package net.malteada.maltecraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.ModEntities;
import net.malteada.maltecraft.item.custom.FuriousArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems {



    // ITEMS
    public static final Item RAW_TANZANITE = registerItem("raw_tanzanite",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));

    //HUEVOS SPAWNERS
    public static final Item HOOR_SPAWN_EGG = registerItem("hoor_spawn_egg",
            new SpawnEggItem(ModEntities.HOOR, 0x0032FC, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item TYR_SPAWN_EGG = registerItem("tyr_spawn_egg",
            new SpawnEggItem(ModEntities.TYR, 0xFFE1AA, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRAGI_SPAWN_EGG = registerItem("bragi_spawn_egg",
            new SpawnEggItem(ModEntities.BRAGI, 0x6E4700, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FORSETI_SPAWN_EGG = registerItem("forseti_spawn_egg",
            new SpawnEggItem(ModEntities.FORSETI, 0xFFFFFF, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item HELLA_SPAWN_EGG = registerItem("hella_spawn_egg",
            new SpawnEggItem(ModEntities.HELLA, 0x00BB04, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item HEIMDALL_SPAWN_EGG = registerItem("heimdall_spawn_egg",
            new SpawnEggItem(ModEntities.HEIMDALL, 0xE400FF, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item JOTTUNHEIM_SPAWN_EGG = registerItem("jottunheim_spawn_egg",
            new SpawnEggItem(ModEntities.JOTTUNHEIM, 0x000000, 0xFF0000,
                    new FabricItemSettings().group(ItemGroup.MISC)));
    // FURIOUS ARMOR
    public static final Item FURIOUS_HELMET = registerItem("furious_helmet",
            new FuriousArmorItem(ModArmorMaterials.RAW_TANZANITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item FURIOUS_CHESTPLATE = registerItem("furious_chestplate",
            new FuriousArmorItem(ModArmorMaterials.RAW_TANZANITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    // Metodos para registrar items, siempre deben de inicializarse
private static Item registerItem(String name, Item item){
    return Registry.register(Registry.ITEM, new Identifier(Maltecraft.MOD_ID, name), item);
}
    public static void registerModItems(){
        Maltecraft.LOGGER.debug("Registering Mods Items for " + Maltecraft.MOD_ID);
    }
}
