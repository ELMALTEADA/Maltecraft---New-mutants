package net.malteada.maltecraft.entity.client;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.entity.custom.HoorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HoorModel extends AnimatedGeoModel<HoorEntity> {
    @Override
    public Identifier getModelResource(HoorEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/hoor.geo.json");
    }

    @Override
    public Identifier getTextureResource(HoorEntity object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/entity/hoor_texture.png");
    }

    @Override
    public Identifier getAnimationResource(HoorEntity animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/hoor.animation.json");
    }
}
