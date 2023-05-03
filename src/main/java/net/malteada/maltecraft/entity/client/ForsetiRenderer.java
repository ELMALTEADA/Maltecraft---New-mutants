package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.BragiEntity;
import net.malteada.maltecraft.entity.custom.ForsetiEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ForsetiRenderer extends GeoEntityRenderer<ForsetiEntity> {
    public ForsetiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ForsetiModel());
        this.shadowRadius=0.6F;
    }
    @Override
    public Identifier getTextureResource(ForsetiEntity instance){
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/forseti_texture.png");
    }
    @Override
    public RenderLayer getRenderType(ForsetiEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation){
        stack.scale(2.0F, 2.0F, 2.0F);
        return super.getRenderType( animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}