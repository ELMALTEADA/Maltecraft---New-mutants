package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.BragiEntity;
import net.malteada.maltecraft.entity.custom.ForsetiEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ForsetiModel extends AnimatedGeoModel<ForsetiEntity> {

    @Override
    public Identifier getModelResource(ForsetiEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/forseti.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForsetiEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/forseti_texture.png");
    }

    @Override
    public Identifier getAnimationResource(ForsetiEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/forseti.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(ForsetiEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180));
        }
    }
}