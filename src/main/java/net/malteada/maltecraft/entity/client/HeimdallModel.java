package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.HeimdallEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HeimdallModel extends AnimatedGeoModel<HeimdallEntity> {

    @Override
    public Identifier getModelResource(HeimdallEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/heimdall.geo.json");
    }

    @Override
    public Identifier getTextureResource(HeimdallEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/heimdall_texture.png");
    }

    @Override
    public Identifier getAnimationResource(HeimdallEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/heimdall.animation.json");
    }

    // This function rotates the head
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(HeimdallEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 90F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 90F));
        }
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head1 = this.getAnimationProcessor().getBone("head1");

        EntityModelData extraData1 = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head1 != null) {
            head.setRotationX(extraData1.headPitch * ((float) Math.PI / 90F));
            head.setRotationY(extraData1.netHeadYaw * ((float) Math.PI / 90F));
        }
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head2 = this.getAnimationProcessor().getBone("head2");

        EntityModelData extraData2 = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head2 != null) {
            head.setRotationX(extraData2.headPitch * ((float) Math.PI / 90F));
            head.setRotationY(extraData2.netHeadYaw * ((float) Math.PI / 90F));
        }
    }

}
