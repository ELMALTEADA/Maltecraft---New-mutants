package net.malteada.maltecraft.entity.client.armor;

import net.malteada.maltecraft.item.custom.FuriousArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class FuriousArmorRenderer extends GeoArmorRenderer<FuriousArmorItem> {
    public FuriousArmorRenderer() {
        super(new FuriousArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";

    }
}
