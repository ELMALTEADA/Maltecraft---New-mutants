package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.HoorEntity;
import net.malteada.maltecraft.entity.custom.TyrEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TyrRenderer extends GeoEntityRenderer<TyrEntity> {
    public TyrRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new TyrModel());
        this.shadowRadius=0.6F;
    }
    @Override
    public Identifier getTextureResource(TyrEntity instance){
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/tyr_texture.png");
    }
    @Override
    public RenderLayer getRenderType(TyrEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation){
        stack.scale(1.3F, 1.3F, 1.3F);
        return super.getRenderType( animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}