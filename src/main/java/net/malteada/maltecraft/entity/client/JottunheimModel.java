package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.JottunheimEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class JottunheimModel extends AnimatedGeoModel<JottunheimEntity> {
    @Override
    public Identifier getModelResource(JottunheimEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/jottunheim.geo.json");
    }

    @Override
    public Identifier getTextureResource(JottunheimEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/jottunheim_texture.png");
    }

    @Override
    public Identifier getAnimationResource(JottunheimEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/jottunheim.animation.json");
    }

    // This function rotates the head
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(JottunheimEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}