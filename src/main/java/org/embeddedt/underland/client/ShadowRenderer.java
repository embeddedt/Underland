package org.embeddedt.underland.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.entity.ShadowEntity;

import javax.annotation.Nonnull;

public class ShadowRenderer extends HumanoidMobRenderer<ShadowEntity, ShadowModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Underland.MODID, "textures/entity/shadow.png");

    public ShadowRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowModel(context.bakeLayer(ShadowModel.SHADOW_LAYER)), 0f);
    }

    @Override
    public RenderType getRenderType(ShadowEntity pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(pLivingEntity);
        return RenderType.itemEntityTranslucentCull(resourcelocation);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(ShadowEntity entity) {
        return TEXTURE;
    }
}
