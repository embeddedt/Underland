package org.embeddedt.underland.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.entity.ShadowEntity;

public class ShadowModel extends HumanoidModel<ShadowEntity> {

    public static final String BODY = "body";

    public static ModelLayerLocation SHADOW_LAYER = new ModelLayerLocation(new ResourceLocation(Underland.MODID, "shadow"), BODY);

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = createMesh(CubeDeformation.NONE, 0.6f);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public ShadowModel(ModelPart part) {
        super(part);
    }
}