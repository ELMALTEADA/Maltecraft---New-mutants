package net.malteada.maltecraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.malteada.maltecraft.entity.ModEntities;
import net.malteada.maltecraft.entity.client.*;
import net.malteada.maltecraft.entity.client.armor.FuriousArmorRenderer;
import net.malteada.maltecraft.item.ModItems;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MaltecraftModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.HOOR, HoorRenderer::new);
        EntityRendererRegistry.register(ModEntities.TYR, TyrRenderer::new);
        EntityRendererRegistry.register(ModEntities.BRAGI, BragiRenderer::new);
        EntityRendererRegistry.register(ModEntities.FORSETI, ForsetiRenderer::new);
        EntityRendererRegistry.register(ModEntities.HELLA, HellaRenderer::new);
        EntityRendererRegistry.register(ModEntities.HEIMDALL, HeimdallRenderer::new);
        EntityRendererRegistry.register(ModEntities.JOTTUNHEIM, JottunheimRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(new FuriousArmorRenderer(), ModItems.FURIOUS_HELMET, ModItems.FURIOUS_CHESTPLATE);
    }
}
