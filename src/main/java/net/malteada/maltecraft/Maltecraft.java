package net.malteada.maltecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.malteada.maltecraft.entity.ModEntities;
import net.malteada.maltecraft.entity.custom.*;
import net.malteada.maltecraft.gen.ModWorldGen;
import net.malteada.maltecraft.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class Maltecraft implements ModInitializer {
	public static final String MOD_ID = "maltecraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		GeckoLib.initialize();
		ModWorldGen.generateWorldGen();
		FabricDefaultAttributeRegistry.register(ModEntities.HOOR, HoorEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.TYR, TyrEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.BRAGI, BragiEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FORSETI, ForsetiEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.HELLA, HellaEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.HEIMDALL, HeimdallEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.JOTTUNHEIM, JottunheimEntity.setAttributes());
	}
}
