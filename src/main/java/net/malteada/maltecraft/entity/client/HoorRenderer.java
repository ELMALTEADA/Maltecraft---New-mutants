package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.HoorEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class HoorRenderer extends GeoEntityRenderer<HoorEntity> {
    public HoorRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HoorModel());
        this.shadowRadius=0.4F;
    }
    @Override
    public Identifier getTextureResource(HoorEntity instance){
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/hoor_texture.png");
    }
    @Override
    public RenderLayer getRenderType(HoorEntity animatable, float partialTicks, MatrixStack stack,
    VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
            int packedLightIn, Identifier textureLocation){
        stack.scale(1.5F, 1.5F, 1.5F);
        return super.getRenderType( animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
