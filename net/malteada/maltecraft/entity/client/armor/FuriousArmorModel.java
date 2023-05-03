package net.malteada.maltecraft.entity.client.armor;

import net.malteada.maltecraft.Maltecraft;
import net.malteada.maltecraft.item.custom.FuriousArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FuriousArmorModel extends AnimatedGeoModel<FuriousArmorItem>  {
    @Override
    public Identifier getModelResource(FuriousArmorItem object) {
        return new Identifier(Maltecraft.MOD_ID, "geo/furiousarmor.geo.json");
    }

    @Override
    public Identifier getTextureResource(FuriousArmorItem object) {
        return new Identifier(Maltecraft.MOD_ID, "textures/models/armor/furiousarmor_texture.png");
    }

    @Override
    public Identifier getAnimationResource(FuriousArmorItem animatable) {
        return new Identifier(Maltecraft.MOD_ID, "animations/furiousarmor.animation.json");
    }
}
