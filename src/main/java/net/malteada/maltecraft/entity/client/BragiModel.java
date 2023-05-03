package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.BragiEntity;
import net.malteada.maltecraft.entity.custom.TyrEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BragiModel extends AnimatedGeoModel<BragiEntity> {

    @Override
    public Identifier getModelResource(BragiEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/bragi.geo.json");
    }

    @Override
    public Identifier getTextureResource(BragiEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/bragi_texture.png");
    }

    @Override
    public Identifier getAnimationResource(BragiEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/bragi.animation.json");
    }

    // This function rotates the head
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(BragiEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 200F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 200F));
        }
    }
}
