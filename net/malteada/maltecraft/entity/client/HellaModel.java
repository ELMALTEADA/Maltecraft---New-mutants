package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.HellaEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HellaModel extends AnimatedGeoModel<HellaEntity> {

    @Override
    public Identifier getModelResource(HellaEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/hella.geo.json");
    }

    @Override
    public Identifier getTextureResource(HellaEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/hella_texture.png");
    }

    @Override
    public Identifier getAnimationResource(HellaEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/hella.animation.json");
    }

    // This function rotates the head
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(HellaEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
